package com.marketcollection.domain.item.controller;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.PageCursor;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApiItemController {

    private final ItemService itemService;

    @PostMapping("/admin/item/new")
    public ResponseEntity<Long> saveItem(@RequestPart(value = "key") ItemFormDto itemFormDto,
                                         @RequestPart(value = "file") List<MultipartFile> itemImageFiles) throws Exception {

        Long itemId = itemService.save(itemFormDto, itemImageFiles);

        return new ResponseEntity<>(itemId, HttpStatus.OK);
    }

    @PostMapping("/admin/item/{itemId}")
    public ResponseEntity<Long> updateItem(@RequestPart(value = "key") ItemFormDto itemFormDto,
                                           @RequestPart(value = "file", required = false) List<MultipartFile> itemImageFiles) throws Exception {

        Long itemId = itemService.updateItem(itemFormDto, itemImageFiles);

        return new ResponseEntity<>(itemId, HttpStatus.OK);
    }
}
