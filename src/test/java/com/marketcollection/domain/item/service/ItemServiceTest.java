package com.marketcollection.domain.item.service;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import com.marketcollection.domain.item.repository.ItemRepository;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemImageRepository itemImageRepository;
    @Autowired ItemRepository itemRepository;

    List<MultipartFile> createItemImageFiles() throws Exception {

        List<MultipartFile> multipartFiles = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            String uploadPath = "D:/develop/resources/market-collection/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    uploadPath, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFiles.add(mockMultipartFile);
        }
        return multipartFiles;
    }

    // 썸네일 라이브러리 사용으로 테스트 중단
    @Disabled
    @Test
    public void save_item() throws Exception {

        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("item1");
        itemFormDto.setOriginalPrice(100);
        itemFormDto.setSalePrice(90);
        itemFormDto.setStockQuantity(100);
        itemFormDto.setDescription("description");
        itemFormDto.setCategoryId(1L);
        itemFormDto.setItemSaleStatus(ItemSaleStatus.ON_SALE);

        List<MultipartFile> multipartFiles = createItemImageFiles();
        Long savedItemId = itemService.save(itemFormDto, multipartFiles);
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(savedItemId);

        Item item = itemRepository.findById(savedItemId).orElseThrow(EntityNotFoundException::new);
        assertThat(multipartFiles.get(0).getOriginalFilename()).isEqualTo(itemImages.get(0).getOriginalFileName());
        assertThat(itemFormDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemFormDto.getItemName()).isEqualTo(item.getItemName());
    }
}