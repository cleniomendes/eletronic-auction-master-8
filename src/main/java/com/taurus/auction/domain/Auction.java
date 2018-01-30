package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Clenio on 22/01/2018.
 */

@Entity
@Table(name = "tb_leilao")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String title;

    @Column(name = "descricao")
    private String description;

    private String banner;

    @Column(name = "dt_hr_inicio")
    private Date start_date;

    @Column(name = "relatorio")
    private String report;

    @Column(name = "simulacao")
    private String simulation;

    @Column(name = "pesquisa_satisfacao")
    private String search;

    @Column(name = "dt_hr_simulacao")
    private Date simulation_date;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Company company;

    private String status;

    @OneToMany(mappedBy = "auction", targetEntity = Stage.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id")
    private Set<Stage> stages;

    @OneToMany(mappedBy = "auction", fetch = FetchType.EAGER)
    private Set<AuctionUser> auctionUsers = new HashSet<AuctionUser>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getSimulation() {
        return simulation;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Date getSimulation_date() {
        return simulation_date;
    }

    public void setSimulation_date(Date simulation_date) {
        this.simulation_date = simulation_date;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public Set<AuctionUser> getAuctionUsers() {
        return auctionUsers;
    }

    public void setAuctionUsers(Set<AuctionUser> auctionUsers) {
        this.auctionUsers = auctionUsers;
    }

    public Set<Stage> getStage() {
        return stages;
    }

    public void setStage(Set<Stage> stages) {
        this.stages = stages;
    }
}
