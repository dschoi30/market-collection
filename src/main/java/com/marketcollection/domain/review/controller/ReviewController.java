package com.marketcollection.domain.review.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.common.exception.ErrorCode;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.service.ItemService;
import com.marketcollection.domain.review.dto.PageRequestDto;
import com.marketcollection.domain.review.dto.PageResponseDto;
import com.marketcollection.domain.review.dto.ReviewDto;
import com.marketcollection.domain.review.exception.ReviewBadReqeustException;
import com.marketcollection.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("review", new ReviewDto(itemFormDto));

        return "/review/reviewForm";
    }

    @PostMapping("/review")
    public ResponseEntity<Long> save(@LoginUser SessionUser user, @Valid @RequestBody ReviewDto reviewDto,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ReviewBadReqeustException(ErrorCode.BINDING_WRONG);
        }
        Long reviewId = reviewService.save(user.getEmail(), reviewDto);

        return new ResponseEntity<Long>(reviewId, HttpStatus.OK);
    }

    @GetMapping("/reviews/{itemId}")
    public @ResponseBody PageResponseDto<ReviewDto> getReivewList(@PathVariable("itemId") Long itemId,
                                                                    PageRequestDto pageRequestDto) {
        return reviewService.getReviewList(itemId, pageRequestDto);
    }
}
