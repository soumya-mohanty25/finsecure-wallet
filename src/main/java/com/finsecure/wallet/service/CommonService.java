package com.finsecure.wallet.service;

import com.finsecure.wallet.common.ServiceOutcome;
import com.finsecure.wallet.model.Citizen;
import com.finsecure.wallet.model.Gender;
import com.finsecure.wallet.model.OtpForUser;
import com.finsecure.wallet.model.User;

import java.util.List;

public interface CommonService {
    OtpForUser findByUserAndisActiveTrueOrderByOtpIdDescLimit1(Long userId);

    ServiceOutcome<Boolean> setOtpIsValidFlag(Long userId, Boolean isValid);

//    List<String> getMobileUserAccessListByIsActiveAndIsMobileAccess(boolean isActive, boolean isMobileAccess);
//
//    List<String> getWebUserAccessListByIsActiveAndIsWebAccess(boolean isActive, boolean isWebAccess);
//
//    ServiceOutcome<String> checkOtpAccessForUser(User user, boolean isActive, boolean isOtpAccess);

    ServiceOutcome<String> generateOTP(String userName,String type);

    ServiceOutcome<List<Gender>> getGenderDetails(Boolean excludeDeactive);

    List<Citizen> getAllCitizenList();

//    boolean blockUser(String type, Long id);

    boolean mailOtpDetails(String subject, User user);

//	public void sendSms(User user);

    Citizen getAllCitizenDetails(Long id);

//    ServiceOutcome<Boolean> saveUserFireBaseTokenMap(UserFireBaseTokenMap map);
//
//    ServiceOutcome<List<UserFireBaseTokenMap>> findUserFireBaseTokenMapByUserId(Long userId);
//
//    ServiceOutcome<AppVersion> getAppLatestVersionAndAppName(String osType, String versionName, String appName);
//
//    ServiceOutcome<UserFireBaseTokenMap> findUserFireBaseTokenMapByTocken(String tocken);

}
