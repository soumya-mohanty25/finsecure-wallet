package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_util_mail_queue")
@Data
public class MailQueued implements Serializable {

    private static final long serialVersionUID = 5034477311179983988L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "mail_to")
    private String mailTo;

    @Column(name = "mail_cc")
    private String mailCc;

    @Column(name = "mail_from")
    private String mailFrom;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "body_type")
    private String bodyType;

    @Column(name = "status")
    private String status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_updated_on")
    private Date lastUpdatedOn;

    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;

}