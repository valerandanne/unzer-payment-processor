package com.unzer.payments.repository;

import com.unzer.payments.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment,Long> {

}
