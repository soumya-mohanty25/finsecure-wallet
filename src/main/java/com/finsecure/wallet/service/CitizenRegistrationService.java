package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.dto.CitizenRegistrationRequestDTO;
import com.finsecure.wallet.dto.ResendOtpRequestDTO;
import com.finsecure.wallet.dto.VerifyOtpRequestDTO;

public interface CitizenRegistrationService {

    ServiceOutcome<String> registerCitizen(CitizenRegistrationRequestDTO requestDTO);

    ServiceOutcome<String> verifyOtp(VerifyOtpRequestDTO dto);

    ServiceOutcome<String> resendOtp(ResendOtpRequestDTO dto);
}
