package com.marketcollection.domain.payment.repository;

import com.marketcollection.domain.payment.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
