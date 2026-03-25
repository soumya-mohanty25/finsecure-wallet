package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

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
}
