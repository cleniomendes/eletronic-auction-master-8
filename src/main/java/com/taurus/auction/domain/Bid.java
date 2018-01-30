package com.taurus.auction.domain;

import javax.persistence.*;

/**
 * Created by Clenio on 25/01/2018.
 */

@Entity
@Table(name = "tb_lance")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_etapa_fase_produto")
    private StageStepProduct stageStepProduct;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    @Column(name = "quantidade")
    private Long quantity;

    @Column(name = "bid_value")
    private Double bidValue;

    @Column(name = "status_lance")
    private String bidStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "quantidade_final")
    private Long quantityFinal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getBidValue() {
        return bidValue;
    }

    public void setBidValue(Double bidValue) {
        this.bidValue = bidValue;
    }

    public String getBidstatus() {
        return bidStatus;
    }

    public void setBidstatus(String bidstatus) {
        this.bidStatus = bidstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StageStepProduct getStageStepProdut() {
        return stageStepProduct;
    }

    public void setStageStepProdut(StageStepProduct stageStepProdut) {
        this.stageStepProduct = stageStepProdut;
    }

    public Long getQuantityFinal() {
        return quantityFinal;
    }

    public void setQuantityFinal(Long quantityFinal) {
        this.quantityFinal = quantityFinal;
    }

}
