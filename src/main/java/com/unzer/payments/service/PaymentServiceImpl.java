package com.unzer.payments.service;

import com.unzer.payments.entity.Payment;
import com.unzer.payments.exception.PaymentNotFoundException;
import com.unzer.payments.model.PaymentRequest;
import com.unzer.payments.model.PaymentResponseDto;
import com.unzer.payments.model.PaymentConfirmation;
import com.unzer.payments.model.PaymentStatus;
import com.unzer.payments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service that operates with payments
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorClient paymentProcessorProxy;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    PaymentServiceImpl(PaymentRepository paymentRepository, PaymentProcessorClient paymentProcessorProxy) {
        this.paymentRepository = paymentRepository;
        this.paymentProcessorProxy = paymentProcessorProxy;
    }

    /**
     * Processes a payment request and saves it as a new {@link Payment}
     *
     * @param paymentToProcess a {@link PaymentRequest}
     * @return the created payment as a {@link PaymentResponseDto}
     */
    @Override
    public PaymentResponseDto processPayment(PaymentRequest paymentToProcess) {
        PaymentConfirmation paymentConfirmation = paymentProcessorProxy.processPayment(paymentToProcess);
        Payment newPayment = createPayment(paymentToProcess, paymentConfirmation);

        Payment createdPayment = paymentRepository.save(newPayment);

        return convertToDto(createdPayment);
    }

    /**
     * Cancels an existent {@link Payment} by changing its status
     *
     * @param paymentId the payment's id
     * @throws PaymentNotFoundException  If the payment that we want to cancel does not exist
     */
    @Override
    public void cancelPayment(long paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) {
            throw new PaymentNotFoundException("The requested payment does not exist");
        }
        payment.get().setStatus(PaymentStatus.CANCELED.name());
        paymentRepository.save(payment.get());
    }

    /**
     * Gets a payment searching by payment id
     *
     * @param paymentId the id of the payment that we are searching for
     * @return a {@link PaymentResponseDto} of the payment or an empty optional if the payment could not be found
     */
    @Override
    public Optional<PaymentResponseDto> getPayment(long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        return payment.map(this::convertToDto);
    }

    /**
     * Creates a {@link Payment} based on the {@link PaymentRequest} and the {@link PaymentConfirmation} information
     *
     * @param paymentRequest the payment request processed
     * @param paymentConfirmation the response of the external processor for the payment request
     * @return a new {@link Payment}
     */
    private Payment createPayment(PaymentRequest paymentRequest, PaymentConfirmation paymentConfirmation) {
        String paymentStatus = paymentConfirmation == null ? PaymentStatus.FAILED.name() : PaymentStatus.APPROVED.name();
        String approvalId = paymentConfirmation == null ? null : paymentConfirmation.getApprovalCode();
        return new Payment(paymentRequest.getAmount(), paymentRequest.getCurrency(), paymentStatus, LocalDateTime.now(),
                approvalId);
    }

    /**
     * Converts a {@link Payment} into a {@link PaymentResponseDto}
     *
     * @param payment the payment entity
     * @return a {@link PaymentResponseDto}
     */
    private PaymentResponseDto convertToDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getAmount(), payment.getCurrency(),
                PaymentStatus.valueOf(payment.getStatus()), payment.getApprovalId());
    }
}
