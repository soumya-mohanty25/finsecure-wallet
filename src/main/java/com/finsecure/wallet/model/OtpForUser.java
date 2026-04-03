package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="t_gaas_otp_for_user")
@Data
public class OtpForUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -300076720371411506L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,nullable = false)
    private Long id;

    @Column(name = "otp_number")
    private String otpNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="is_valid")
    private Boolean isValid=true;

    @Column(name="valid_till")
    private Date validTill;

    @Column(name="request_time")
    private Date requestTime;

    @Column(name = "temp_user_name")
    private String tempUserName;


}

