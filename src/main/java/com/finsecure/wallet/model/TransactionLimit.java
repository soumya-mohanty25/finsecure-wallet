package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_transaction_limit")
public class TransactionLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_limit_id")
    private Long limitId;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "single_txn_limit")
    private BigDecimal singleTxnLimit;
}
