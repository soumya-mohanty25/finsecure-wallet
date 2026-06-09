package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_fund_transfer")
public class FundTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne
    private Wallet senderWallet;

    @ManyToOne
    private Wallet receiverWallet;

    private BigDecimal amount;

    private String status;

    private Date transferDate;

    private String remarks;
}