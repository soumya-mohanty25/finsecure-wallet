package com.finsecure.wallet.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_wallet_block_history")
public class WalletBlockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_block_id")
    private Long blockId;

    @ManyToOne
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "action_date")
    private Date actionDate;
}
