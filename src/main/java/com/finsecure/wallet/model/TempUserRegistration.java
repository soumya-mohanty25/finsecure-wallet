package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "t_mst_temp_user_registration")
public class TempUserRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name = "id_proof_type")
    private String idProofType;

    @Column(name = "id_proof_no", unique = true)
    private String idProofNo;

    @Column(name = "id_proof_doc_path")
    private String idProofDocPath;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "state")
    private Long state;

    @Column(name = "district")
    private Long district;

    @Column(name = "city")
    private Long city;

    @Column(name = "pin", length = 10)
    private String pin;

    @Column(name = "profile_picture_path")
    private String profilePicturePath;

    @Column(name = "is_blocked")
    private Boolean isBlocked=false;

    @Column(name = "designation")
    private String designation;

    @Column(name = "organization")
    private String organization;

    @Transient
    private String profilePictureByte;

    @Column(name = "otp_number")
    private String otpNumber;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_expired")
    private Boolean isExpired = false;

    @Column(name = "otp_valid_till")
    private Date otpValidTill;

    @Column(name = "alternative_number", length = 15)
    private String alternateNo;
}

