package com.taurus.auction.repository;

import com.taurus.auction.domain.Auction;
import com.taurus.auction.domain.AuctionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Clenio on 22/01/2018.
 */
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Auction findAuctionsByAuctionUsers(Set<AuctionUser> user);

    List<Auction> findByStatus(String status);

    Auction findById(Long id);
}
