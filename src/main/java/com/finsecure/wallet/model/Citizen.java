package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_mst_citizen")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citizen_id")
    private Long citizenId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "id_proof_no", unique = true)
    private String idProofNo;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "profile_picture_path")
    private String profilePicturePath;
}
