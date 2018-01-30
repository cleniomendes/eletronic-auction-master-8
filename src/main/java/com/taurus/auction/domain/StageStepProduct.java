package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Clenio on 22/01/2018.
 */

@Entity
@Table(name = "tb_etapa_fase_produto")
public class StageStepProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_etapa_fase")
    private StageStep stageStep;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Product product;

    @Column(name = "tipo")
    private String type;

    @Column(name = "qtd_ofertada")
    private Long qtOffer;

    @ManyToOne
    @JoinColumn(name = "id_unidade")
    private Unit unit;

    private char status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public StageStep getStageStep() {
        return stageStep;
    }

    public void setStageStep(StageStep stageStep) {
        this.stageStep = stageStep;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getQtOffer() {
        return qtOffer;
    }

    public void setQtOffer(Long qtOffer) {
        this.qtOffer = qtOffer;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
