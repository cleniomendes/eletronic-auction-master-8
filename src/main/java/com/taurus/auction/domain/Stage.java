package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Clenio on 22/01/2018.
 */
@Entity
@Table(name = "tb_etapa")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="descricao")
    private String description;

    @Column(name = "dt_hr_inicio")
    private Date start_date;

    @Column(name = "dt_hr_fim")
    private Date end_date;

    @ManyToOne
    @JoinColumn(name = "id_leilao")
    private Auction auction;

    private String status;

    @OneToMany(mappedBy = "stage", targetEntity = StageStep.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id")
    private Set<StageStep> stageStep;

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

    @JsonIgnore
    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<StageStep> getStageStep() {
        return stageStep;
    }

    public void setStageStep(Set<StageStep> stageStep) {
        this.stageStep = stageStep;
    }
}
