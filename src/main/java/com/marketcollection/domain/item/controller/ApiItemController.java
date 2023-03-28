package com.marketcollection.domain.item.controller;

import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApiItemController {

    private final ItemService itemService;

    @PostMapping("/admin/item/new")
    public ResponseEntity saveItem(@Valid @RequestPart(value = "key") ItemFormDto itemFormDto, BindingResult bindingResult,
                                           @RequestPart(value = "file") List<MultipartFile> itemImageFiles) throws Exception {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            sb.append(fieldErrors.get(0).getDefaultMessage());

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Long itemId = itemService.save(itemFormDto, itemImageFiles);

        return new ResponseEntity<Long>(itemId, HttpStatus.OK);
    }

    @PostMapping("/admin/item/{itemId}")
    public ResponseEntity updateItem(@Valid @RequestPart(value = "key") ItemFormDto itemFormDto, BindingResult bindingResult,
                                           @RequestPart(value = "file", required = false) List<MultipartFile> itemImageFiles) throws Exception {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            sb.append(fieldErrors.get(0).getDefaultMessage());

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        Long itemId = itemService.updateItem(itemFormDto, itemImageFiles);

        return new ResponseEntity<>(itemId, HttpStatus.OK);
    }
}
