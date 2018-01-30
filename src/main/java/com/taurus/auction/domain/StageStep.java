package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Clenio on 22/01/2018.
 */
@Entity
@Table(name = "tb_etapa_fase")
public class StageStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descricao")
    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "id_etapa")
    Stage stage;

    @Column(name = "duracao")
    private Long duration;

    @Column(name = "habilitar")
    private char enable;

    @Column(name = "status_lance")
    private char bid_status;

    @Column(name = "exibir_lance")
    private char bid_show;

    @Column(name = "intervalo_duracao")
    private Long duration_interval;

    @Column(name = "prorrogacao_restante")
    private char remaining_extension;

    @OneToMany(mappedBy = "stageStep", targetEntity = StageStepProduct.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<StageStepProduct> stageStepProducts;

    @Column(name = "dt_hr_inicio")
    private Date start_date;

    @Column(name = "dt_hr_fim")
    private Date end_date;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public char getEnable() {
        return enable;
    }

    public void setEnable(char enable) {
        this.enable = enable;
    }

    public char getBid_status() {
        return bid_status;
    }

    public void setBid_status(char bid_status) {
        this.bid_status = bid_status;
    }

    public char getBid_show() {
        return bid_show;
    }

    public void setBid_show(char bid_show) {
        this.bid_show = bid_show;
    }

    public Long getDuration_interval() {
        return duration_interval;
    }

    public void setDuration_interval(Long duration_interval) {
        this.duration_interval = duration_interval;
    }

    public char getRemaining_extension() {
        return remaining_extension;
    }

    public void setRemaining_extension(char remaining_extension) {
        this.remaining_extension = remaining_extension;
    }

    public Set<StageStepProduct> getStageStepProducts() {
        return stageStepProducts;
    }

    public void setStageStepProducts(Set<StageStepProduct> stageStepProducts) {
        this.stageStepProducts = stageStepProducts;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}
