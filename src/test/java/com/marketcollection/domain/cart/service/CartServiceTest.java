package com.marketcollection.domain.cart.service;

import com.marketcollection.domain.cart.Cart;
import com.marketcollection.domain.cart.CartItem;
import com.marketcollection.domain.cart.dto.CartRequestDto;
import com.marketcollection.domain.cart.repository.CartItemRepository;
import com.marketcollection.domain.cart.repository.CartRepository;
import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.*;
import com.marketcollection.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired CartService cartService;
    @Autowired CartRepository cartRepository;
    @Autowired CartItemRepository cartItemRepository;

    public Member saveMember() {
        Member member = new Member(1L, SocialType.NAVER, "test@gmail.com", "test1",
                "01012341234", new Address(), 0, Role.USER, Grade.NORMAL, MemberStatus.ACTIVE);
        return memberRepository.save(member);
    }
    public Item saveItem() {
        Item item = new Item("test1", 10000, 9000, 10000, "content",
                1L, "", 0, 0, 0, ItemSaleStatus.ON_SALE);
        return itemRepository.save(item);
    }
    public CartItem saveCart() {
        Member member = saveMember();
        Item item = saveItem();
        Cart cart = new Cart(member);
        cartRepository.save(cart);
        CartItem cartItem = new CartItem(cart, item, 5);
        return cartItemRepository.save(cartItem);
    }

    @DisplayName("장바구니 상품 추가 테스트")
    @Test
    public void addCart() {
        //given
        Member member = saveMember();
        Item item = saveItem();

        //when
        CartRequestDto cartRequestDto = new CartRequestDto(item.getId(), 5);
        Long cartItemId = cartService.addCart(member.getEmail(), cartRequestDto);

        //then
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        assertThat(cartItem).hasFieldOrPropertyWithValue("count", 5);
        assertThat(cartItem).hasFieldOrPropertyWithValue("item", item);
    }

    @DisplayName("장바구니 상품 수량 변경")
    @Test
    public void updateCartItem() {
        //given
        CartItem cartItem = saveCart();

        //when
        cartItem.updateCount(100);

        //then
        assertThat(cartItem).hasFieldOrPropertyWithValue("count", 100);
    }
}