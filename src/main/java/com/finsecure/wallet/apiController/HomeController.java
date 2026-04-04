package com.finsecure.wallet.apiController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/login")
    public String getLogin(
            Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                return "redirect:/";
            }
            //model.addAttribute("dist", citizenProfileService.getAllDist(citizenProfileByMobileNo.getData().getState()));
        } catch (Exception e) {
            log.error("",e);
            log.error("Error occured while forward",e);
        }
        return "layout.landing";
    }

    @GetMapping("/officialLogin")
    public String officialLogin(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        try {
            if (user != null) {
                return "redirect:/home";
            }
        } catch (Exception e) {
            log.error("Error Occured",e);
        }
        return "layout.login";
    }
}
