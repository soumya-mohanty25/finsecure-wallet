package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_wallet_topup")
public class WalletTopup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topup_id")
    private Long topupId;

    @ManyToOne
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "status")
    private String status;

    @Column(name = "transaction_date")
    private Date transactionDate;
}