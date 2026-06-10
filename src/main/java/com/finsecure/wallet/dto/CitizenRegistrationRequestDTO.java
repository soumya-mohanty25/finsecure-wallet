package com.finsecure.wallet.dto;

import lombok.Data;

@Data
public class CitizenRegistrationRequestDTO {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String alternateNo;

    private String emailId;

    private String gender;

    private String address;

    private Long state;

    private Long district;

    private Long city;

    private String pin;

    private String designation;

    private String organization;

    private String idProofType;

    private String idProofNo;

    private String profilePictureByte;

    private String idProofDocByte;

    private String password;

    private String confirmPassword;
}
