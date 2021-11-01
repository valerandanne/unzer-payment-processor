package com.unzer.payments.controller;

import com.unzer.payments.model.PaymentRequest;
import com.unzer.payments.model.PaymentResponseDto;
import com.unzer.payments.model.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * Contains Integration Tests for {@link PaymentProcessorController}
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentProcessorControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Value("${external-processor-config.url}")
    private String externalProcessorUrl;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;
    private TestRestTemplate testRestTemplate;

    private final String LOCAL_BASE_URL = "http://localhost:";

    @BeforeEach
    void setUp() {
        testRestTemplate = new TestRestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }


    @Test
    void processPaymentSuccessfully() {
        server.expect(ExpectedCount.once(), requestTo(externalProcessorUrl + "/process"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{ \"approvalCode\" : \"7658b7c8.c679.41a0.8d8c.942efdcd8c96\"}",
                        MediaType.APPLICATION_JSON));
        PaymentRequest paymentRequest = buildPaymentRequest();
        ResponseEntity<PaymentResponseDto> paymentResponse = testRestTemplate.postForEntity(LOCAL_BASE_URL + port
                        + "/payments/process",
                paymentRequest, PaymentResponseDto.class);
        System.out.println(paymentResponse);
        assertThat(paymentResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(paymentResponse.getBody()).extracting(PaymentResponseDto::getAmount, PaymentResponseDto::getCurrency,
                PaymentResponseDto::getApprovalId, PaymentResponseDto::getStatus)
                .containsExactly(paymentRequest.getAmount(), paymentRequest.getCurrency(),
                        "7658b7c8.c679.41a0.8d8c.942efdcd8c96",
                        PaymentStatus.APPROVED);

    }

    @Test
    void processPaymentWhenExternalProcessorServiceFails() {
        server.expect(ExpectedCount.once(), requestTo("http://localhost:8500/process"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        PaymentRequest paymentRequest = buildPaymentRequest();
        ResponseEntity<PaymentResponseDto> paymentResponse = testRestTemplate.postForEntity(LOCAL_BASE_URL + port
                        + "/payments/process",
                paymentRequest, PaymentResponseDto.class);

        assertThat(paymentResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(paymentResponse.getBody()).extracting(PaymentResponseDto::getAmount, PaymentResponseDto::getCurrency,
                PaymentResponseDto::getApprovalId, PaymentResponseDto::getStatus)
                .containsExactly(paymentRequest.getAmount(), paymentRequest.getCurrency(),
                        null,
                        PaymentStatus.FAILED);
    }

    @Test
    void getPaymentStatusForExistentPayment() {
        server.expect(ExpectedCount.once(), requestTo(externalProcessorUrl + "/process"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{ \"approvalCode\" : \"7658b7c8.c679.41a0.8d8c.942efdcd8c96\"}",
                        MediaType.APPLICATION_JSON));
        PaymentRequest paymentRequest = buildPaymentRequest();
        ResponseEntity<PaymentResponseDto> paymentCreationResponse = testRestTemplate.postForEntity(LOCAL_BASE_URL
                        + port + "/payments/process",
                paymentRequest, PaymentResponseDto.class);
        long createdPaymentId = paymentCreationResponse.getBody().getId();

        ResponseEntity<PaymentResponseDto> paymentRetrievalResponse = testRestTemplate.getForEntity(LOCAL_BASE_URL
                + port + "/payments/" + createdPaymentId, PaymentResponseDto.class);
        assertThat(paymentRetrievalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(paymentCreationResponse.getBody()).isEqualTo(paymentCreationResponse.getBody());
    }

    @Test
    void getPaymentStatusForNotFoundPayment() {
        ResponseEntity<String> paymentRetrievalResponse = testRestTemplate.getForEntity(LOCAL_BASE_URL
                + port + "/payments/" + 11111L, String.class);
        assertThat(paymentRetrievalResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(paymentRetrievalResponse.getBody()).isEqualTo("The requested payment does not exist");
    }

    @Test
    void cancelExistentPaymentSuccessfully() {
        server.expect(ExpectedCount.once(), requestTo(externalProcessorUrl + "/process"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{ \"approvalCode\" : \"7658b7c8.c679.41a0.8d8c.942efdcd8c96\"}",
                        MediaType.APPLICATION_JSON));
        PaymentRequest paymentRequest = buildPaymentRequest();
        ResponseEntity<PaymentResponseDto> paymentCreationResponse = testRestTemplate.postForEntity(LOCAL_BASE_URL
                        + port + "/payments/process", paymentRequest, PaymentResponseDto.class);
        long createdPaymentId = paymentCreationResponse.getBody().getId();

        testRestTemplate.put(LOCAL_BASE_URL + port + "/payments/{id}/cancel", null, createdPaymentId);

        ResponseEntity<PaymentResponseDto> paymentRetrievalResponse = testRestTemplate.getForEntity(LOCAL_BASE_URL
                + port + "/payments/" + createdPaymentId, PaymentResponseDto.class);
        PaymentResponseDto canceledPayment = paymentRetrievalResponse.getBody();
        assertThat(canceledPayment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    void cancelNotFoundPayment() {
        ResponseEntity<Void> cancelResponse = testRestTemplate.exchange(LOCAL_BASE_URL + port
                + "/payments/115500/cancel", HttpMethod.PUT, null, Void.class);
        assertThat(cancelResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private PaymentRequest buildPaymentRequest() {
        return new PaymentRequest("5556234665645656", LocalDateTime.now(),
                123, 200D, "$");
    }
}