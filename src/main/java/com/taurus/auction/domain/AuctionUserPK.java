package com.taurus.auction.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Clenio on 22/01/2018.
 */

@Embeddable
public class AuctionUserPK implements Serializable{

    @Column(name = "id_leilao")
    private Long auction_id;

    @Column(name = "id_usuario")
    private Long user_id;

    public Long getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Long auction_id) {
        this.auction_id = auction_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
