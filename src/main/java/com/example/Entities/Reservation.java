package com.example.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer numTables;

    @Column(nullable = false)
    private Integer numChairs;

    @Column(name = "restaurant_id", nullable = false)
    private int restaurantId;

    public Reservation(){

    }
}
