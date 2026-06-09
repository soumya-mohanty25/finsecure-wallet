package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    private String moduleName;

    private String action;

    private Long userId;

    private Date actionTime;

    private String ipAddress;
}