package com.marketcollection.domain.payment.repository;

import com.marketcollection.domain.payment.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
