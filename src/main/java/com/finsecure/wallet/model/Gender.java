package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name  =  "t_mst_gender")
@Data
public class Gender implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private Long genderId;

    @Column(name = "gender_name_en")
    private String genderNameEn;

    @Column(name = "gender_name_hi")
    private String genderNameHi;

    @Column(name = "gender_code")
    private String genderCode;

    @Column(name = "is_active")
    private Boolean isActive=true;
}

