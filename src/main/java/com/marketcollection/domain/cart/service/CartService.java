package com.marketcollection.domain.cart.service;

import com.marketcollection.domain.cart.Cart;
import com.marketcollection.domain.cart.CartItem;
import com.marketcollection.domain.cart.dto.CartItemDto;
import com.marketcollection.domain.cart.dto.CartRequestDto;
import com.marketcollection.domain.cart.repository.CartItemRepository;
import com.marketcollection.domain.cart.repository.CartRepository;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // 장바구니 상품 추가
    public Long addCart(String memberId, CartRequestDto cartRequestDto) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartRequestDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), cartRequestDto.getItemId());

        if(cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, cartRequestDto.getCount());
            cartItemRepository.save(cartItem);
        } else {
            cartItem.addCount(cartRequestDto.getCount()); // 장바구니에 해당 상품이 이미 존재하는 경우 수량 추가
        }
        return cartItem.getId();
    }

    // 장바구니 목록 조회
    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItemList(String memberId) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMemberId(member.getId());

        List<CartItemDto> cartItemDtos = new ArrayList<>();

        if(cart == null) {
            return cartItemDtos;
        }
        cartItemDtos = cartItemRepository.findCartItemList(cart.getId());

        return cartItemDtos;
    }

    // 주문 완료 후 장바구니 상품 제거
    public void deleteCartItemsAfterOrder(Long memberId, List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            CartItem cartItem = cartItemRepository.findByMemberIdAndItemId(memberId, item.getId());
            cartItemRepository.delete(cartItem);
        }
    }

    // 주문자 유효성 검사
    @Transactional(readOnly = true)
    public boolean validateCartItem(String email, Long cartItemId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }

    // 장바구니 상품 수량 변경
    public void updateCartItem(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    // 장바구니 상품 제거
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
