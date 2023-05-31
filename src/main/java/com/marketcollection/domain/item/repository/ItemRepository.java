package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
    Optional<Item> findWithPessimisticLockById(Long itemId);
}
