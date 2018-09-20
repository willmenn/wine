package io.wine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    private int stock;
}
