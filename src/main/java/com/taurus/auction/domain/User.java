package com.taurus.auction.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Clenio on 17/01/18.
 */
@Entity
@Table(name = "tb_usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_usuario")
    private String username;

    @Column(name = "nome")
    private String name;

    private String email;

    @JsonIgnore
    @Column(name = "senha")
    private String password;

    @OneToOne
    @JoinColumn(name = "id_usuario_tipo", nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private Company company;

    private String status;

    @OneToMany(mappedBy = "user")
    private Set<AuctionUser> auctionUsers = new HashSet<AuctionUser>();

    private Long lastro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Set<AuctionUser> getAuctionUsers() {
        return auctionUsers;
    }

    public void setAuctionUsers(Set<AuctionUser> auctionUsers) {
        this.auctionUsers = auctionUsers;
    }

    public Long getLastro() {
        return lastro;
    }

    public void setLastro(Long lastro) {
        this.lastro = lastro;
    }
}

