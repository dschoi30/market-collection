package com.marketcollection.domain.cart.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.cart.dto.CartItemDto;
import com.marketcollection.domain.cart.dto.CartRequestDto;
import com.marketcollection.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;

    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity<Long> addCart(@LoginUser SessionUser user, @RequestBody CartRequestDto cartRequestDto) {
        Long cartId = cartService.addCart(user.getEmail(), cartRequestDto.getItemId(), cartRequestDto.getCount());

        return new ResponseEntity<Long>(cartId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String getCartItemList(Model model, @LoginUser SessionUser user) {
        List<CartItemDto> cartItems = cartService.getCartItemList(user.getEmail());

        model.addAttribute("cartItems", cartItems);

        return "cart/cart";
    }

    @PatchMapping("/cart/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@LoginUser SessionUser user, @PathVariable("cartItemId") Long cartItemId, int count) {
        if(count <= 0) {
            return new ResponseEntity<String>("상품을 최소 1개 이상 담아야 합니다.", HttpStatus.FORBIDDEN);
        } else if(!cartService.validateCartItem(user.getEmail(), cartItemId)) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        cartService.updateCartItem(cartItemId, count);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@LoginUser SessionUser user, @PathVariable("cartItemId") Long cartItemId) {
        if(!cartService.validateCartItem(user.getEmail(), cartItemId)) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        };
        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
