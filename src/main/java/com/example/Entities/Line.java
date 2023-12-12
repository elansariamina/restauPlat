package com.example.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/*@Data
@Entity
//@Table(name = "lines")*/
public class Line implements Serializable {
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    @CollectionTable(name = "line_properties", joinColumns = @JoinColumn(name = "line_id"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    private Map<String, Object> properties = new HashMap<>();
*/
}