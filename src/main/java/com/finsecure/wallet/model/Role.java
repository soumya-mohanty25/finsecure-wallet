package com.finsecure.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@Table(name = "t_mst_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "display_on_page")
    private Boolean displayOnPage;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "max_assignments")
    private Long maxAssignments;

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
