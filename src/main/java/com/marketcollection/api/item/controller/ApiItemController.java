package com.marketcollection.api.item.controller;

import com.marketcollection.api.item.service.ApiItemService;
import com.marketcollection.api.item.dto.ItemFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApiItemController {

    private final ApiItemService apiItemService;

    @PostMapping("/admin/item/new")
    public ResponseEntity<Long> saveItem(Model model,
                                         @RequestPart(value = "key") ItemFormDto itemFormDto,
                                         @RequestPart(value = "file", required = false) List<MultipartFile> itemImageFiles) {
        Long itemId = null;

        try {
            itemId = apiItemService.save(itemFormDto, itemImageFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
        }
        return new ResponseEntity<>(itemId, HttpStatus.OK);
    }
}
