package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Clenio on 22/01/2018.
 */
@Entity
@Table(name = "tb_leilao_participantes")
public class AuctionUser implements Serializable {
    @EmbeddedId
    private AuctionUserPK id;

    @ManyToOne
    @MapsId("auction_id")
    @JoinColumn(name = "id_leilao")
    private Auction auction;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "id_usuario")
    private User user;

    //additional column
    private String status;

    public AuctionUserPK getId() {
        return id;
    }

    public void setId(AuctionUserPK id) {
        this.id = id;
    }

    @JsonIgnore
    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
