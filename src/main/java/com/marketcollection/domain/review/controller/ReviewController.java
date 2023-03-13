package com.marketcollection.domain.review.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.service.ItemService;
import com.marketcollection.domain.review.dto.ReviewFormDto;
import com.marketcollection.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemService itemService;

    // 헤더에 회원 정보 출력
    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    @GetMapping("/review/{itemId}/new")
    public String getReviewForm(@PathVariable Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.findById(itemId);
        model.addAttribute("review", new ReviewFormDto(itemFormDto));

        return "/review/reviewForm";
    }

    @PostMapping("/review")
    public ResponseEntity<Long> save(@LoginUser SessionUser user, @RequestBody ReviewFormDto reviewFormDto) {
        Long reviewId = reviewService.save(user.getEmail(), reviewFormDto);

        return new ResponseEntity<Long>(reviewId, HttpStatus.OK);
    }
}
