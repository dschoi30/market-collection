package com.marketcollection.domain.payment.repository;

import com.marketcollection.domain.payment.EasyPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EasyPayRepository extends JpaRepository<EasyPay, Long> {
}
