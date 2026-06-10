package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_wallet_transaction")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_transaction_id")
    private Long transactionId;

    @Column(name = "transaction_ref_no")
    private String transactionRefNo;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_type")
    private String transactionType; // CREDIT, DEBIT

    @Column(name = "transaction_status")
    private String transactionStatus; // SUCCESS, FAILED, PENDING

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "transaction_date")
    private Date transactionDate;
}
