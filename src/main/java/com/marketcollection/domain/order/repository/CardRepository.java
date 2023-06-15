package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
