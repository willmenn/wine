package io.wine.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> wineId;
}
