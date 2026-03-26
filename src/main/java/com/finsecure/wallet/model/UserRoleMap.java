package com.finsecure.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@Table(name = "t_mst_user_role_map")
public class UserRoleMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_map_id")
    private Long userRoleId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "is_active")
    private Boolean isActive;

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
