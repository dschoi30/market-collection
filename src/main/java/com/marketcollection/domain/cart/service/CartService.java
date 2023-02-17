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

    public Long addCart(String memberId, Long itemId, int count) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId);

        if(cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, count);
            cartItemRepository.save(cartItem);
        } else {
            cartItem.addCount(count);
        }
        return cart.getId();
    }

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

    public void removeCartItems(Long memberId, List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            CartItem cartItem = cartItemRepository.findByMemberIdAndItemId(memberId, item.getId());
            cartItemRepository.delete(cartItem);
        }
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public boolean validateCartItem(String email, Long cartItemId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }
}
