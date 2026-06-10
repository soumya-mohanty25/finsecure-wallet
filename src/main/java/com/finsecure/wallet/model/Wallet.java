package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "wallet_number")
    private String walletNumber;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "wallet_status")
    private String walletStatus; // ACTIVE, BLOCKED, CLOSED

    @Column(name = "created_on")
    private Date createdOn;
}
