package com.marketcollection.domain.point.service;

import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.point.Point;
import com.marketcollection.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PointService {

    private final PointRepository pointRepository;

    public void createOrderPoint(Member member, OrderItem orderItem) {

        Point point = Point.createOrderPoint(member, orderItem);
        pointRepository.save(point);
    }

    public void createUsingPoint(Member member, OrderDto orderDto) {
        Point point = Point.createUsingPoint(member, orderDto);
        pointRepository.save(point);
    }
}
