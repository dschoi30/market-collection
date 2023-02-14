package com.marketcollection.domain.order.service;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderItemDto;
import com.marketcollection.domain.order.dto.OrderItemRequestDto;
import com.marketcollection.domain.order.dto.OrderRequestDto;
import com.marketcollection.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public OrderDto setOrderInfo(String memberId, OrderRequestDto orderRequestDto) {
        OrderDto orderDto = new OrderDto();
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        orderDto.setMemberInfo(member);

        OrderItemRequestDto orderItemRequestDto = orderRequestDto.getOrderItemRequestDtos().get(0);
        Item item = itemRepository.findById(orderItemRequestDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        OrderItemDto orderItemDto = new OrderItemDto(item.getId(), item.getItemName(), item.getSalePrice(),
                orderItemRequestDto.getCount());
        orderDto.setOrderItemDtos(Arrays.asList(orderItemDto));

        return orderDto;
    }

    public Long order(String memberId, OrderDto orderDto) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        member.updateOrderInfo(orderDto.getPhoneNumber(), orderDto.getZipCode(), orderDto.getAddress(), orderDto.getDetailAddress());

        List<OrderItemDto> orderItemDtos = orderDto.getOrderItemDtos();
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto orderItemDto : orderItemDtos) {
            Item item = itemRepository.findById(orderItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem(item, orderItemDto.getCount());

            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItems);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }
        orderRepository.save(order);

        return order.getId();
    }
}
