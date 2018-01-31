package com.taurus.auction.controller;

import com.taurus.auction.domain.Auction;
import com.taurus.auction.repository.AuctionRepository;
import com.taurus.auction.schedule.AuctionScheduled;
import com.taurus.auction.service.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Clenio on 22/01/2018.
 */

@RestController
@RequestMapping("/api/auction")
public class AuctionController implements Serializable {
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionScheduled auctionScheduled;

    @Autowired
    private AuctionRepository auctionRepository;

    private static final Logger log = LoggerFactory.getLogger(AuctionController.class);

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public ResponseEntity getAuctions() {
        List<Auction> auctions = auctionService.findAllAuctions();

        if (auctions != null) {
            log.info(String.format("Found %s auctions in database", auctions.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(auctions);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public ResponseEntity getAuctionByUser(@RequestBody String username) {
        List<Auction> auctions = auctionService.findAuctionByUser(username);

        return ResponseEntity.status(HttpStatus.OK).body(auctions);
    }

    @GetMapping(value = "/start")
    public void startAuctions() throws Exception {
        auctionService.startAuctions();
    }

    @PostMapping(value = "/create")
    @PatchMapping(value = "/update")
    public ResponseEntity createAuction(@RequestBody Auction auction) {
        try {
            auctionRepository.saveAndFlush(auction);
            log.info(String.format("save auction=%s success", auction.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(auction);
    }

    @DeleteMapping
    public ResponseEntity deleteAuction(@RequestBody Auction auction) {
        try {
            auctionRepository.delete(auction);
            log.info(String.format("delete auction=%s success", auction.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(auction);
    }
}
