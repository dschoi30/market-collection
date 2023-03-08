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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;

    // 헤더에 회원 정보 출력
    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    // 장바구니 상품 추가
    @PostMapping("/cart")
    public @ResponseBody ResponseEntity addCart(@LoginUser SessionUser user, @RequestBody @Valid CartRequestDto cartRequestDto,
                                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Long cartItemId;
        try {
            cartItemId = cartService.addCart(user.getEmail(), cartRequestDto);
        } catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 상품 목록 조회
    @GetMapping("/cart")
    public String getCartItemList(Model model, @LoginUser SessionUser user) {
        List<CartItemDto> cartItems = cartService.getCartItemList(user.getEmail());

        model.addAttribute("cartItems", cartItems);

        return "cart/cart";
    }

    // 장바구니 상품 수량 변경
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

    // 장바구니 상품 삭제
    @DeleteMapping("/cart/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@LoginUser SessionUser user, @PathVariable("cartItemId") Long cartItemId) {
        if(!cartService.validateCartItem(user.getEmail(), cartItemId)) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        };
        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
