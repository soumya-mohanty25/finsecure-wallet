package com.finsecure.wallet.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_mst_citizen")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citizen_id")
    private Long citizenId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;
}
