package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "action")
    private String action;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action_time")
    private Date actionTime;

    @Column(name = "ip_address")
    private String ipAddress;
}