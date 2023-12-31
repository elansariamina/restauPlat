package com.example.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Entity
@Table(name = "restaurants")
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String number;

    @Column(name = "disponibleTables")
    private Integer disponibleTables;

    public Restaurant(){

    }
}
