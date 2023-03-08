package com.marketcollection.domain.common;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, MultipartFile multipartFile) throws Exception {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String renamedFileName = uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, renamedFileName));
        return renamedFileName;
    }

    public String createThumbnailImage(String uploadPath, String originalFileName, MultipartFile multipartFile) throws Exception {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String renamedFileName = "t-" + uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, renamedFileName));
        Thumbnails.of(new File(uploadPath, renamedFileName))
                .forceSize(500, 500)
                .toFile(new File(uploadPath, renamedFileName));
        return renamedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File file = new File(filePath);

        if(file.exists()) {
            file.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

    @RequiredArgsConstructor
    @Controller
    public static class MainController {

        private final ItemService itemService;

        @GetMapping("/")
        public String mainPage(Model model, @LoginUser SessionUser user, ItemSearchDto itemSearchDto,
                               Optional<Integer> page, HttpServletRequest request) {
            if(user != null) {
                model.addAttribute("userName", user.getUserName());
            }

            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
            Page<ItemListDto> items = itemService.getItemListPage(itemSearchDto, pageable);
            List<ItemListDto> recentItems = itemService.getRecentViewList(request);
            model.addAttribute("items", items);
            model.addAttribute("recentItems", recentItems);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("maxPage", 10);

            return "main";
        }
    }
}
