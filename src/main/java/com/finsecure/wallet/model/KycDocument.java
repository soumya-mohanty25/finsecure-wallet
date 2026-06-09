package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_kyc_document")
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    private Citizen citizen;

    private String documentType;

    private String documentNumber;

    private String documentPath;

    private String verificationStatus;

    private Date uploadedOn;
}
