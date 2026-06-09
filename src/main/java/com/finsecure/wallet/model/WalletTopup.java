package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_wallet_topup")
public class WalletTopup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topupId;

    @ManyToOne
    private Wallet wallet;

    private BigDecimal amount;

    private String paymentMode;

    private String paymentReference;

    private String status;

    private Date transactionDate;
}