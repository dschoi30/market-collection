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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

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
            cartItem.addCount(cartRequestDto.getCount());
        }
        return cart.getId();
    }
}
