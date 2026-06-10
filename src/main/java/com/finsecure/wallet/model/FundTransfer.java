package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_fund_transfer")
public class FundTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long transferId;

    @ManyToOne
    @JoinColumn(name = "sender_wallet")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet")
    private Wallet receiverWallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private String status;

    @Column(name = "transfer_date")
    private Date transferDate;

    @Column(name = "remarks")
    private String remarks;
}