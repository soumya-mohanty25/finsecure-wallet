package com.finsecure.wallet.utils;

import com.finsecure.wallet.model.LoggedInUser;
import com.finsecure.wallet.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {
    public static User getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            LoggedInUser currentUser = (LoggedInUser)auth.getPrincipal();
            return currentUser.getDbUser();
        } catch (Exception var2) {
            return null;
        }
    }
}
