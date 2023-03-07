package com.marketcollection.domain.item.controller;

import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApiItemController {

    private final ItemService itemService;

    @PostMapping("/admin/item/new")
    public ResponseEntity saveItem(BindingResult bindingResult, @RequestPart(value = "key") ItemFormDto itemFormDto,
                                         @RequestPart(value = "file") List<MultipartFile> itemImageFiles) throws Exception {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<String>("상품 등록 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        Long itemId = itemService.save(itemFormDto, itemImageFiles);

        return new ResponseEntity<Long>(itemId, HttpStatus.OK);
    }

    @PostMapping("/admin/item/{itemId}")
    public ResponseEntity<Long> updateItem(@RequestPart(value = "key") ItemFormDto itemFormDto,
                                           @RequestPart(value = "file", required = false) List<MultipartFile> itemImageFiles) throws Exception {

        Long itemId = itemService.updateItem(itemFormDto, itemImageFiles);

        return new ResponseEntity<>(itemId, HttpStatus.OK);
    }
}
