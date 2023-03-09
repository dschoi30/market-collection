package com.marketcollection.domain.point.service;

import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.repository.OrderItemRepository;
import com.marketcollection.domain.point.Point;
import com.marketcollection.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public void createOrderPoint(String memberId, OrderItem orderItem) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);

        Point point = Point.createOrderPoint(member, orderItem);
        pointRepository.save(point);
    }

    public void createUsingPoint(String memberId, OrderDto orderDto) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        Point point = Point.createUsingPoint(member, orderDto);
    }
}
