package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
