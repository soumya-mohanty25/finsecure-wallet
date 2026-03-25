package com.finsecure.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_mst_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "dob")
    private Date dateOfBirth;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private Boolean isEnabled;

    @Column(name = "designation")
    private String designation;

    @Column(name = "allow_multiple_session")
    private Boolean allowMultipleSession;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "is_logged_in")
    private Boolean isLoggedIn;

    @Column(name = "wrong_login_cnt")
    private Integer wrongLoginCount;

    @ManyToOne
    @JoinColumn(name = "primary_role_id")
    private Role primaryRole;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Transient
    private List<Role> roles;

    @Column(name = "last_request_time")
    private Date lastRequestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @CreatedDate
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by")
    @JsonIgnore
    private User lastUpdatedBy;

    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date lastUpdatedOn;
}
