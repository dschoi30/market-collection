package com.marketcollection.common.main;

import com.marketcollection.common.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String mainPage(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
        return "main";
    }
}
