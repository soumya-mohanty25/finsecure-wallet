package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.dto.CitizenRegistrationRequestDTO;
import com.finsecure.wallet.dto.ResendOtpRequestDTO;
import com.finsecure.wallet.dto.VerifyOtpRequestDTO;
import com.finsecure.wallet.model.MailQueued;
import com.finsecure.wallet.model.TempUserRegistration;
import com.finsecure.wallet.repository.MailQueuedRepository;
import com.finsecure.wallet.repository.TempUserRegistrationRepository;
import com.finsecure.wallet.utils.EmailTemplateUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@Transactional
public class CitizenRegistrationServiceImpl implements CitizenRegistrationService{

    @Autowired
    private TempUserRegistrationRepository tempUserRegistrationRepository;

    @Autowired
    private MailQueuedRepository mailQueuedRepository;

    @Value("${mail.username}")
    private String mailFrom;

    @Override
    public ServiceOutcome<String> registerCitizen(
            CitizenRegistrationRequestDTO requestDTO) {

        ServiceOutcome<String> svcOutcome =
                new ServiceOutcome<>();

        try {

            log.info(
                    "Citizen registration initiated for email : {}",
                    requestDTO.getEmailId());

            /*
             * Password Validation
             */
            if (!requestDTO.getPassword()
                    .equals(requestDTO.getConfirmPassword())) {

                log.warn(
                        "Password mismatch for email : {}",
                        requestDTO.getEmailId());

                return ServiceOutcome.failure(
                        "Password and Confirm Password do not match");
            }

            /*
             * Phone Validation
             */
            if (tempUserRegistrationRepository
                    .existsByPhoneNumber(
                            requestDTO.getPhoneNumber())) {

                log.warn(
                        "Phone number already registered : {}",
                        requestDTO.getPhoneNumber());

                return ServiceOutcome.failure(
                        "Phone Number already registered");
            }

            /*
             * Email Validation
             */
            if (tempUserRegistrationRepository
                    .existsByEmailId(
                            requestDTO.getEmailId())) {

                log.warn(
                        "Email already registered : {}",
                        requestDTO.getEmailId());

                return ServiceOutcome.failure(
                        "Email ID already registered");
            }

            String otp = generateOtp();

            TempUserRegistration tempUser =
                    new TempUserRegistration();

            tempUser.setName(
                    requestDTO.getFirstName()
                            + " "
                            + requestDTO.getLastName());

            tempUser.setPhoneNumber(
                    requestDTO.getPhoneNumber());

            tempUser.setAlternateNo(
                    requestDTO.getAlternateNo());

            tempUser.setEmailId(
                    requestDTO.getEmailId());

            tempUser.setGender(
                    requestDTO.getGender());

            tempUser.setAddress(
                    requestDTO.getAddress());

            tempUser.setState(
                    requestDTO.getState());

            tempUser.setDistrict(
                    requestDTO.getDistrict());

            tempUser.setCity(
                    requestDTO.getCity());

            tempUser.setPin(
                    requestDTO.getPin());

            tempUser.setDesignation(
                    requestDTO.getDesignation());

            tempUser.setOrganization(
                    requestDTO.getOrganization());

            tempUser.setIdProofType(
                    requestDTO.getIdProofType());

            tempUser.setIdProofNo(
                    requestDTO.getIdProofNo());

            tempUser.setOtpNumber(otp);

            tempUser.setOtpValidTill(
                    new Date(
                            System.currentTimeMillis()
                                    + (5 * 60 * 1000)));

            tempUser.setIsVerified(false);
            tempUser.setIsExpired(false);
            tempUser.setIsBlocked(false);

            tempUserRegistrationRepository.save(tempUser);

            queueOtpMail(
                    requestDTO.getEmailId(),
                    requestDTO.getFirstName(),
                    otp);

            log.info(
                    "Temp registration created successfully for email : {}",
                    requestDTO.getEmailId());

            svcOutcome.setOutcome(true);
            svcOutcome.setMessage(
                    "Registration successful. OTP has been sent to your registered email.");

            return svcOutcome;

        } catch (Exception ex) {

            log.error(
                    "Error occurred while citizen registration for email : {}",
                    requestDTO.getEmailId(),
                    ex);

            svcOutcome.setOutcome(false);
            svcOutcome.setMessage(
                    "Unable to process registration request. Please try again later.");

            return svcOutcome;
        }
    }

    /**
     * Generate 6 digit OTP
     */
    private String generateOtp() {

        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 1000000));
    }

    private void queueOtpMail(String emailId,
                              String citizenName,
                              String otp) {

        try {

            String emailBody =
                    EmailTemplateUtil.registrationOtpTemplate(
                            citizenName,
                            otp
                    );

            MailQueued mailQueue = new MailQueued();

            mailQueue.setMailTo(emailId);
            mailQueue.setMailFrom(mailFrom);   // IMPORTANT

            mailQueue.setSubject(
                    EmailTemplateUtil.REGISTER_PASSWORD_OTP);

            mailQueue.setBody(emailBody);
            mailQueue.setBodyType("HTML");
            mailQueue.setStatus("QUEUED");

            mailQueue.setCreatedBy(1L);
            mailQueue.setCreatedOn(new Date());

            mailQueuedRepository.save(mailQueue);

            log.info("OTP mail queued successfully for {}", emailId);

        } catch (Exception e) {

            log.error(
                    "Error while queueing OTP email for : {}",
                    emailId,
                    e
            );

            throw e;
        }
    }

    @Override
    @Transactional
    public ServiceOutcome<String> verifyOtp(
            VerifyOtpRequestDTO dto) {

        try {

            TempUserRegistration tempUser =
                    tempUserRegistrationRepository.findByEmailId(dto.getEmailId());

            if (tempUser == null) {

                return ServiceOutcome.failure(
                        "User not found");
            }

            if (Boolean.TRUE.equals(tempUser.getIsVerified())) {

                return ServiceOutcome.failure(
                        "User already verified");
            }

            if (!tempUser.getOtpNumber()
                    .equals(dto.getOtp())) {

                return ServiceOutcome.failure(
                        "Invalid OTP");
            }

            if (tempUser.getOtpValidTill()
                    .before(new Date())) {

                return ServiceOutcome.failure(
                        "OTP expired");
            }

            tempUser.setIsVerified(true);

            tempUserRegistrationRepository.save(tempUser);

            return ServiceOutcome.success(
                    null,
                    "OTP verified successfully");

        } catch (Exception ex) {

            log.error(
                    "Error while verifying OTP",
                    ex);

            return ServiceOutcome.failure(
                    "OTP verification failed");
        }
    }

    @Override
    @Transactional
    public ServiceOutcome<String> resendOtp(
            ResendOtpRequestDTO dto) {

        try {

            TempUserRegistration tempUser =
                    tempUserRegistrationRepository
                            .findByEmailId(dto.getEmailId());

            if (tempUser == null) {

                return ServiceOutcome.failure(
                        "User not found");
            }

            if (Boolean.TRUE.equals(
                    tempUser.getIsVerified())) {

                return ServiceOutcome.failure(
                        "User already verified");
            }

            String otp = generateOtp();

            tempUser.setOtpNumber(otp);

            tempUser.setOtpValidTill(
                    new Date(
                            System.currentTimeMillis()
                                    + (5 * 60 * 1000)));

            tempUserRegistrationRepository.save(tempUser);

            queueOtpMail(
                    tempUser.getEmailId(),
                    tempUser.getName(),
                    otp);

            return ServiceOutcome.success(
                    null,
                    "OTP resent successfully");

        } catch (Exception ex) {

            log.error(
                    "Error while resending OTP",
                    ex);

            return ServiceOutcome.failure(
                    "Unable to resend OTP");
        }
    }
}
