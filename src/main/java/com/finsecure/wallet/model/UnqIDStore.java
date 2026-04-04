package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_mst_unq_store", schema = "public")
public class UnqIDStore implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unq_id")
    private Long id;

    @Column(name = "unq_token_id")
    private String unqTokenID;

    @Column(name = "unq_time")
    private Date token_time;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

}
