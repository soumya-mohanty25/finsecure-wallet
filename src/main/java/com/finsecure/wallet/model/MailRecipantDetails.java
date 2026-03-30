package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "t_util_mail_recipants")
@Data
public class MailRecipantDetails implements Serializable {

    private static final long serialVersionUID = -4359119379145174137L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "type")
    private String type;

}
