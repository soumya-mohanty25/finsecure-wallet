package com.finsecure.wallet.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String userName;
    private String password;
    private String captcha;
    private String appCode;
}