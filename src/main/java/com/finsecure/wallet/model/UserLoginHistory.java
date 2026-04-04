package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "t_umt_user_login_history",
        schema = "public"
)
@Data
public class UserLoginHistory implements Serializable {
    private static final long serialVersionUID = 3649339921550049779L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "user_login_history_id"
    )
    private Long userLoginHistoryId;
    @Column(
            name = "user_id"
    )
    private Long userId;
    @Column(
            name = "user_name"
    )
    private String userName;
    @Column(
            name = "first_name"
    )
    private String firstName;
    @Column(
            name = "last_name"
    )
    private String lastName;
    @Column(
            name = "mobile"
    )
    private String mobile;
    @Column(
            name = "email"
    )
    private String email;
    @Column(
            name = "ip_address"
    )
    private String ipAddress;
    @Column(
            name = "logged_in_date"
    )
    private Date loggedInDate;
    @Column(
            name = "logged_out_date"
    )
    private Date loggedOutDate;
    @Column(
            name = "browser_details"
    )
    private String browserDetails;
    @Column(
            name = "os_details"
    )
    private String osDetails;
    @Column(
            name = "login_type"
    )
    private String loginType;
    @Column(
            name = "login_status"
    )
    private String loginStatus;

}