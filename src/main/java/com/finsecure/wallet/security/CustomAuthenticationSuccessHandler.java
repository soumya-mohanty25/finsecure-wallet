package com.finsecure.wallet.security;

import com.finsecure.wallet.model.LoggedInUser;
import com.finsecure.wallet.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        HttpSession session = request.getSession();

        Object principal = authentication.getPrincipal();

        if (principal instanceof LoggedInUser) {
            LoggedInUser loggedInUser = (LoggedInUser) principal;
            User user = loggedInUser.getDbUser();

            boolean isSecurityRole = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SECURITY"));

            if (isSecurityRole) {
                session.setMaxInactiveInterval(168 * 60 * 60);
                log.info("ROLE_SECURITY → session timeout 12 Hours");
            } else {
                session.setMaxInactiveInterval(2 * 60 * 60);
                log.info("Normal role → session timeout 30 minutes");
            }

            log.info("Logged in user: " + user.getUserName());
        }
    }
}
