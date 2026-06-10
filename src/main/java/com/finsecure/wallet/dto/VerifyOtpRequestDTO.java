package com.finsecure.wallet.dto;

import lombok.Data;

@Data
public class VerifyOtpRequestDTO {

    private String emailId;

    private String otp;
}
