package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_kyc_document")
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_path")
    private String documentPath;

    @Column(name = "verification_status")
    private String verificationStatus;

    @Column(name = "uploaded_on")
    private Date uploadedOn;
}
