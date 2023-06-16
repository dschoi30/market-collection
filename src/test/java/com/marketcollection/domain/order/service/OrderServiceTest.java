package com.marketcollection.domain.order.service;

import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.*;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;

    public Member saveMember() {
        Member member = new Member(1L, SocialType.NAVER, "test@gmail.com", "test1",
                "01012341234", new Address(), 0, Role.USER, Grade.NORMAL, MemberStatus.ACTIVE);
        return memberRepository.save(member);
    }
    public Item saveItem() {
        Item item = new Item("test1", 10000, 9000, 0, 10000, "content",
                1L, "", 0, 0, 0, ItemSaleStatus.ON_SALE);
        return itemRepository.save(item);
    }
    public Order saveOrder() {
        Member member = saveMember();
        Item item = saveItem();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem(item, null, item.getRepImageUrl(), item.getSalePrice());
        orderItems.add(orderItem);
        Order order = new Order(member, orderItems, member.getPhoneNumber(), new Address(), OrderStatus.DONE);
        return orderRepository.save(order);
    }

    @DisplayName("주문 테스트")
    @Test
    public void order() {
        //given
        Member member = saveMember();
        Item item = saveItem();

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderItemDto orderItemDto = new OrderItemDto(item, 1, 0.01);
        orderItemDtos.add(orderItemDto);
        OrderDto orderDto = new OrderDto(member.getMemberName(), "01012341234", 12345, "서울시", "강남", 0, 10000, 100, 0, 10000, orderItemDtos, "Y");

        //when
        OrderResponseDto orderResponseDto = orderService.order(member.getEmail(), orderDto);

        //then
        Order order = orderRepository.findByOrderNumber(orderResponseDto.getOrderNumber()).orElseThrow(EntityNotFoundException::new);
        String itemName = order.getOrderItems().get(0).getItem().getItemName();
        assertThat(itemName).isEqualTo(item.getItemName());
    }

    @DisplayName("관리자 주문 관리 목록 조회 테스트")
    @Test
    public void getAdminOrderList() {
        //given
        Order order = saveOrder();

        //when
        OrderSearchDto orderSearchDto = new OrderSearchDto("3m", null, null, null);
        Page<AdminOrderDto> adminOrderList = orderService.getAdminOrderList(orderSearchDto, PageRequest.of(0, 10));

        //then
        assertThat(adminOrderList).extracting("repItemName").contains("test1");
    }

    @DisplayName("주문 취소 테스트")
    @Test
    public void cancelOrder() {
        //given
        Order order = saveOrder();

        //when
        order.cancelOrder();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Disabled
    @DisplayName("주문자 유효성 검사 테스트")
    @Test
    public void validateOrder() {
        //given
        Order order = saveOrder();

        //when
        boolean isValid = orderService.validateOrder(order.getOrderNumber(), saveMember().getEmail());

        //then
        assertThat(isValid).isTrue();
    }

    @DisplayName("주문자 유효성 검사 실패 테스트")
    @Test
    public void validateUnusualOrder() {
        //given
        Order order = saveOrder();

        //then
        assertThatThrownBy(() -> orderService.validateOrder(order.getOrderNumber(), "fakeAccount")).isInstanceOf(EntityNotFoundException.class);
    }
}