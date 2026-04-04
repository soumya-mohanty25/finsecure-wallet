package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.*;
import com.finsecure.wallet.repository.*;
import com.finsecure.wallet.utils.RandomString;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OtpForUserRepository otpForUserRepository;

//    @Autowired
//    private AppVersionRepository appVersionRepository;

//    @Autowired
//    private UserAccessRepository userAccessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

//    @Autowired
//    private MessageTemplateRepository messageTemplateRepository;
//    @Autowired
//    private SmsService smsService;

    @Autowired
    private CommonService commonService;

//    @Autowired
//    private UserFireBaseTokenMapRepository userFireBaseTokenMapRepository;

    @Autowired
    private CitizenRepository citizenProfileRepository;

    @Autowired
    private MailQueuedRepository mailQueuedRepository;

    @Autowired
    private TempUserRegistrationRepository tempUserRegistrationRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${mail.username}")
    private String MAIL_FROM;

    ResourceBundle rb = ResourceBundle.getBundle("application");

    @Override
    public OtpForUser findByUserAndisActiveTrueOrderByOtpIdDescLimit1(Long userId) {
        OtpForUser otp = otpForUserRepository.findByUserAndisActiveTrueOrderByOtpIdDescLimit1(userId);
        if(otp!=null) {
            Long milliseconds = otp.getValidTill().getTime() - otp.getRequestTime().getTime();
            Long mins = milliseconds/60000;
            if(mins > Long.parseLong(rb.getString("OTP.IN.MINS"))) {
                otp.setIsValid(false);
                otpForUserRepository.save(otp);
                return null;
            }else {
                return otp;
            }
        }
        return otp;
    }

    @Override
    public ServiceOutcome<Boolean> setOtpIsValidFlag(Long userId, Boolean isValid) {
        ServiceOutcome<Boolean> outcome = new ServiceOutcome<>();
        try {
            otpForUserRepository.updateRecordsByUserIdAndIsValid(userId,isValid);
            outcome.setData(true);
            outcome.setMessage("Success.");
            outcome.setOutcome(true);
        } catch (Exception ex) {
            log.error("Exception occured in setOtpIsValidFlag method in CommonServiceImpl-->",ex);
            outcome.setData(null);
            outcome.setMessage("Unable to check otp.");
            outcome.setOutcome(false);
        }
        return outcome;
    }

    @Transactional
    @Override
    public ServiceOutcome<String> generateOTP(String userName,String type) {
        ServiceOutcome<String> outcome = new ServiceOutcome<>();
        try {
            LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(Long.parseLong(rb.getString("OTP.IN.MINS")), ChronoUnit.MINUTES));
            Date validTill = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

            RandomString rs = new RandomString(6, new SecureRandom(), "123456789");
            String newOTP = rs.nextString();
            User user = userRepository.findByUserName(userName);
            if(user != null) {
                OtpForUser request = new OtpForUser();
                request.setUser(user);
                request.setIsValid(true);
                request.setValidTill(validTill);
                request.setRequestTime(new Date());
                request.setOtpNumber(newOTP);
                request = otpForUserRepository.save(request);
                //User userObj = new User();
                String mobileNumber = user.getMobile();
                if(type.equals("login")) {
                    if(user.getMobile()!=null && !user.getMobile().isEmpty()) {
                        //whatsappService.sendToSingleUser(user.getMobile(), loginWPToken, new ArrayList<>(Arrays.asList(newOTP)));
                        //smsService.sendSMS( msgg.getSmsContent().replace(ApplicationConstants.LOGIN_OTP, newOTP),mobileNumber, msgg);
                    }
                }else if(type.equals("register")) {
                    if(user.getMobile()!=null && !user.getMobile().isEmpty()) {
                        //whatsappService.sendToSingleUser(user.getMobile(), registerWPToken, new ArrayList<>(Arrays.asList(newOTP)));
                        //smsService.sendSMS( msgg.getSmsContent().replace(ApplicationConstants.REGISTER_OTP, newOTP),mobileNumber, msgg);
                    }
                }else {
                    //reset password
                    if(user.getMobile()!=null && !user.getMobile().isEmpty()) {
                        //whatsappService.sendToSingleUser(user.getMobile(), resetOTPWPToken, new ArrayList<>(Arrays.asList(newOTP)));
                        //smsService.sendSMS( msgg.getSmsContent().replace(ApplicationConstants.RESET_OTP, newOTP),mobileNumber, msgg);
                    }
                }
                outcome.setOutcome(true);
                outcome.setMessage("OTP has been successfully sent to your mobile number.");
                outcome.setData(newOTP);
                return outcome;
            }

            TempUserRegistration temp = tempUserRegistrationRepository.findByPhoneNumber(userName);
            if (temp != null) {
                temp.setOtpNumber(newOTP);
                //temp.setOtpNumber("123456");
                temp.setOtpValidTill(validTill);
                tempUserRegistrationRepository.save(temp);

//	            MessageTemplate msg = commonService.findByTemplateName(loginSMSToken);
//	            smsService.sendSMS(
//	                    msg.getSmsContent().replace("YYYY", newOTP).replace("XXXX", temp.getPhoneNumber()),
//	                    temp.getPhoneNumber(), msg);
                if(temp.getPhoneNumber()!=null && !temp.getPhoneNumber().isEmpty()) {
                    //whatsappService.sendToSingleUser(temp.getPhoneNumber(), registerWPToken, new ArrayList<>(Arrays.asList(newOTP)));
                }

                outcome.setOutcome(true);
                outcome.setMessage("OTP sent successfully to your mobile number.");
                outcome.setData(newOTP);
                return outcome;
            }
            outcome.setOutcome(false);
            outcome.setMessage("Invalid userName.");
            outcome.setData(newOTP);
            return outcome;
        }
        catch (Exception ex) {
            log.error("Exception occured in generateOTP method in CommonServiceImpl-->",ex);
            outcome.setData(null);
            outcome.setMessage("Unable to generate otp.");
            outcome.setOutcome(false);
        }
        return outcome;

    }

    @Override
    public ServiceOutcome<List<Gender>> getGenderDetails(Boolean excludeDeactive) {
        ServiceOutcome<List<Gender>> outcome = new ServiceOutcome<>();
        List<Gender> genderList = new ArrayList<>();
        try {
            if(excludeDeactive) {
                genderList = genderRepository.findAllByIsActiveTrueOrderByGenderIdAsc();
            }else {
                genderList = genderRepository.findAll();
            }
            outcome.setData(genderList);
            outcome.setMessage("Gender details fetched successfully.");
            outcome.setOutcome(true);
            return outcome;
        } catch (Exception ex) {
            log.error("Exception occured in getGenderDetails method in CommonServiceImpl-->",ex);
            outcome.setData(null);
            outcome.setMessage("Unable to get gender details.");
            outcome.setOutcome(false);
        }
        return outcome;
    }

    @Override
    public List<Citizen> getAllCitizenList() {
        List<Citizen> collect = citizenProfileRepository.findAll(Sort.by("id"));

        collect.forEach(c -> c.setIdProofNo(safeAadhaar(c.getIdProofNo())));
        return collect;
    }

//    @Override
//    public boolean blockUser(String type, Long id) {
//        Citizen citizen = citizenProfileRepository.findById(id).get();
//        if (type.equals("citizen")) {
//            citizen.setIsBlocked(!citizen.getIsBlocked());
//            citizenProfileRepository.save(citizen);
//
//        }
//        return citizen.getIsBlocked();
//    }

    //-------------------------------------------------------------------------------

    public boolean mailOtpDetails(String subject, User user) {
        try {
            String emailId = MAIL_FROM;

            if (subject.equals("APPOINTMENT")) {

                Citizen citizenProfile = citizenProfileRepository.findByPhoneNumber(user.getMobile());

                String body = "<html>" +
                        "<body>" +
                        "<p>Hello " + user.getFirstName() + ",</p>" +
                        "<p>Your appointment is currently <b>pending</b>.</p>" +
                        "<p>We will notify you once it is confirmed.</p>" +
                        "</body>" +
                        "</html>";

                MailQueued mObj = new MailQueued();
                mObj.setMailFrom(emailId);
                mObj.setMailTo(citizenProfile.getEmailId());
                mObj.setSubject(subject);
                mObj.setCreatedOn(new Date());
                mObj.setCreatedBy(user.getUserId());
                mObj.setLastUpdatedOn(new Date());
                mObj.setBody(body);
                mObj.setBodyType("HTML");
                mObj.setStatus("PROCESSING");

                return mailQueuedRepository.save(mObj) != null;

            } else if (subject.equals("RESET PASSWORD")) {

                String newPassword = "123456";

                String body = "<html>" +
                        "<body>" +
                        "<p>Hello " + user.getFirstName() + ",</p>" +
                        "<p>Your password has been reset.</p>" +
                        "<p><b>New Password: " + newPassword + "</b></p>" +
                        "<p>Please change it after logging in.</p>" +
                        "</body>" +
                        "</html>";

                MailQueued mObj = new MailQueued();
                mObj.setMailFrom(emailId);
                mObj.setMailTo(user.getEmail());
                mObj.setSubject(subject);
                mObj.setCreatedOn(new Date());
                mObj.setCreatedBy(user.getUserId());
                mObj.setLastUpdatedOn(new Date());
                mObj.setBody(body);
                mObj.setBodyType("HTML");
                mObj.setStatus("PROCESSING");

                mObj = mailQueuedRepository.save(mObj);

                if (mObj != null) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                }
            }

            return false;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public Citizen getAllCitizenDetails(Long id) {
        Citizen profile = citizenProfileRepository.findById(id).get();

        if (profile != null) {
            String profilePicturePath = profile.getProfilePicturePath();

            if (profilePicturePath != null && !profilePicturePath.trim().isEmpty()) {
                String fullPath = rb.getString("UPLOAD.FILE.PATH") + File.separator + profilePicturePath;
                File imageFile = new File(fullPath);

                if (imageFile.exists() && imageFile.isFile()) {
                    try {
                        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//                        profile.setProfilePictureByte(base64Image);
                    } catch (IOException e) {
                        log.error("Error reading profile picture: " + e.getMessage());
                        profile.setProfilePicturePath(null);
                    }
                } else {
                    System.err.println("Profile picture not found at path: " + fullPath);
                    profile.setProfilePicturePath(null);
                }
            }
        }

        return profile;
    }


//    @Override
//    public ServiceOutcome<Boolean> saveUserFireBaseTokenMap(UserFireBaseTokenMap map) {
//        ServiceOutcome<Boolean> outcome = new ServiceOutcome<>();
//        try {
//            userFireBaseTokenMapRepository.save(map);
//            outcome.setData(true);
//            outcome.setMessage("Success.");
//            outcome.setOutcome(true);
//        } catch (Exception ex) {
//            log.error("Exception occured in saveUserFireBaseTokenMap method in CommonServiceImpl-->",ex);
//            outcome.setData(null);
//            outcome.setMessage("Unable to save firebase token.");
//            outcome.setOutcome(false);
//        }
//        return outcome;
//    }

//    @Override
//    public ServiceOutcome<List<UserFireBaseTokenMap>> findUserFireBaseTokenMapByUserId(Long userId) {
//        ServiceOutcome<List<UserFireBaseTokenMap>> outcome = new ServiceOutcome<>();
//        try {
//            List<UserFireBaseTokenMap> map = userFireBaseTokenMapRepository.findAllByUserUserId(userId);
//            if(map != null) {
//                outcome.setData(map);
//                outcome.setMessage("Success.");
//                outcome.setOutcome(true);
//            }else {
//                outcome.setData(null);
//                outcome.setMessage("No UserFireBaseTokenMap found.");
//                outcome.setOutcome(false);
//            }
//
//        } catch (Exception ex) {
//            log.error("Exception occured in saveUserFireBaseTokenMap method in CommonServiceImpl-->",ex);
//            outcome.setData(null);
//            outcome.setMessage("Unable to save firebase token.");
//            outcome.setOutcome(false);
//        }
//        return outcome;
//    }

//    @Override
//    public ServiceOutcome<UserFireBaseTokenMap> findUserFireBaseTokenMapByTocken(String tocken) {
//        ServiceOutcome<UserFireBaseTokenMap> outcome = new ServiceOutcome<>();
//        try {
//            UserFireBaseTokenMap map = userFireBaseTokenMapRepository.findByFirebaseToken(tocken);
//            if(map != null) {
//                outcome.setData(map);
//                outcome.setMessage("Success.");
//                outcome.setOutcome(true);
//            }else {
//                outcome.setData(null);
//                outcome.setMessage("No UserFireBaseTokenMap found.");
//                outcome.setOutcome(false);
//            }
//
//        } catch (Exception ex) {
//            log.error("Exception occured in saveUserFireBaseTokenMap method in CommonServiceImpl-->",ex);
//            outcome.setData(null);
//            outcome.setMessage("Unable to save firebase token.");
//            outcome.setOutcome(false);
//        }
//        return outcome;
//    }

    public static String getLastFourChars(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        if (input.length() <= 4) {
            return input;
        }
        return input.substring(input.length() - 4);
    }

    private String safeAadhaar(String maskedAadhaar) {
        if (maskedAadhaar == null || maskedAadhaar.length() < 4)
            return "XXXX-XXXX-XXXX";
        return "XXXX-XXXX-" + getLastFourChars(maskedAadhaar);
    }

}

