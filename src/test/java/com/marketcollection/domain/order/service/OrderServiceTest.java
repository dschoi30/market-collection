package com.marketcollection.domain.order.service;

import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.MemberStatus;
import com.marketcollection.domain.member.Role;
import com.marketcollection.domain.member.SocialType;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.AdminOrderDto;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderItemDto;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import com.marketcollection.domain.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;

    public Member saveMember() {
        Member member = new Member(1L, SocialType.NAVER, "test@gmail.com", "test1", 01012341234, new Address(), 0, Role.USER, MemberStatus.ACTIVE);
        return memberRepository.save(member);
    }
    public Item saveItem() {
        Item item = new Item("test1", 10000, 9000, 10000, "content", 1L, 0, 0, ItemSaleStatus.ON_SALE);
        return itemRepository.save(item);
    }

    @DisplayName("주문 테스트")
    @Test
    public void order() {
        Member member = saveMember();
        Item item = saveItem();
        OrderDto orderDto = new OrderDto();
        orderDto.setMemberInfo(member);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderItemDto orderItemDto = new OrderItemDto(item.getId(), item.getItemName(), item.getSalePrice(), 1);
        orderItemDtos.add(orderItemDto);
        orderDto.setOrderItemDtos(orderItemDtos);
        Long orderId = orderService.order(member.getEmail(), orderDto);

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        String itemName = order.getOrderItems().get(0).getItem().getItemName();
        assertThat(itemName).isEqualTo(item.getItemName());
    }
}