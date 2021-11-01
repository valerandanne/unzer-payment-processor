package com.unzer.payments.controller;

import com.unzer.payments.exception.PaymentNotFoundException;
import com.unzer.payments.model.PaymentRequest;
import com.unzer.payments.model.PaymentResponseDto;
import com.unzer.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "/payments")
public class PaymentProcessorController {

    private final PaymentService paymentService;

    @Autowired
    PaymentProcessorController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponseDto paymentResponseDto = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{paymentId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable long paymentId) throws PaymentNotFoundException {
        paymentService.cancelPayment(paymentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{paymentId}", produces = "application/json")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable long paymentId) throws PaymentNotFoundException {
        Optional<PaymentResponseDto> payment = paymentService.getPayment(paymentId);
        if (payment.isEmpty()) {
            throw new PaymentNotFoundException("The requested payment does not exist");
        }
        return new ResponseEntity<>(payment.get(), HttpStatus.OK);
    }
}
