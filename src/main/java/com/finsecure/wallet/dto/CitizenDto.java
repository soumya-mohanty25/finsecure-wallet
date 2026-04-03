package com.finsecure.wallet.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CitizenDto {
    private Long citizenId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailId;
    private String gender;
    private String address;
    private String profilePicturePath;
    private MultipartFile profilepicture;
}
