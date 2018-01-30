package com.taurus.auction.domain;

import javax.persistence.*;

/**
 * Created by Clenio on 22/01/2018.
 */

@Entity
@Table(name = "tb_unidade_medida")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="descricao")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
