package com.finsecure.wallet.apiController;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.dto.CitizenRegistrationRequestDTO;
import com.finsecure.wallet.dto.ResendOtpRequestDTO;
import com.finsecure.wallet.dto.VerifyOtpRequestDTO;
import com.finsecure.wallet.service.CitizenRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/citizen")
public class CitizenRegistrationController {

    @Autowired
    private CitizenRegistrationService citizenRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<ServiceOutcome<String>> registerCitizen(
            @RequestBody CitizenRegistrationRequestDTO dto) {

        return ResponseEntity.ok(
                citizenRegistrationService.registerCitizen(dto));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ServiceOutcome<String>> verifyOtp(
            @RequestBody VerifyOtpRequestDTO dto) {

        return ResponseEntity.ok(
                citizenRegistrationService.verifyOtp(dto));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ServiceOutcome<String>> resendOtp(
            @RequestBody ResendOtpRequestDTO dto) {

        return ResponseEntity.ok(
                citizenRegistrationService.resendOtp(dto));
    }
}
