package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    ServiceOutcome<Boolean> createFailedLoginHistory(User user, HttpServletRequest request, HttpServletResponse httpServletResponse);

    ServiceOutcome<Boolean> createNoUserLoginHistory(String userName, HttpServletRequest request, HttpServletResponse httpServletResponse);

    ServiceOutcome<Boolean> createSuccessLoginHistory(User user, HttpServletRequest request, HttpServletResponse response);

    Boolean isFirstLogin();

}
