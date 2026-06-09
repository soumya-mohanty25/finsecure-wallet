package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_transaction_limit")
public class TransactionLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal dailyLimit;

    private BigDecimal monthlyLimit;

    private BigDecimal singleTxnLimit;
}
