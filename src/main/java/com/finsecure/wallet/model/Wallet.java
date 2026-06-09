package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(unique = true)
    private String walletNumber;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    private BigDecimal balance;

    private String walletStatus; // ACTIVE, BLOCKED, CLOSED

    private Date createdOn;
}
