package com.taurus.auction.service;

import com.taurus.auction.domain.Auction;
import com.taurus.auction.domain.User;
import com.taurus.auction.repository.AuctionRepository;
import com.taurus.auction.repository.UserRepository;
import com.taurus.auction.schedule.AuctionScheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Clenio on 22/01/2018.
 */
@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionScheduled auctionScheduled;

    private static final Logger log = LoggerFactory.getLogger(AuctionService.class);

    public List<Auction> findAllAuctions() {
        return (List<Auction>) auctionRepository.findAll();
    }

    public List<Auction> findAuctionByUser(String username) {
        List<Auction> auctions = new ArrayList<>();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.error(String.format("User not found"));
            throw new UsernameNotFoundException(String.format("User not found"));
        }
        user.getAuctionUsers().stream().filter(auctionUser -> auctionUser.getStatus().equals("A")).forEach(auctionUser -> auctions.add(auctionUser
                .getAuction()));

        if (auctions.isEmpty()) {
            log.info(String.format("Not found auction for user %s", username));
        }

        return auctions;
    }

    public void startAuctions() throws Exception {
        List<Auction> auctions = auctionRepository.findByStatus("E");

        if (!auctions.isEmpty()) {
            if (auctions.size() > 1) {
                auctions.forEach(auction -> {
                    callScheduleAuction(auction);
                });
            } else {
                callScheduleAuction(auctions.get(0));
            }
        }
    }

    public void callScheduleAuction(Auction auction) {
        //format date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        try {
            //If have auction for today date, will start scheduler task
            if (df.format(date).equals(df.format(auction.getStart_date()))) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

                Runnable task = () -> auctionScheduled.startAuction(auction.getId(), executor);
                executor.scheduleAtFixedRate(task, 2, 10, TimeUnit.SECONDS);
            } else {
                log.warn(String.format("No one auction for %s", df.format(date)));
            }
        } catch (Exception e) {
            log.error("Task run not start auction");
            throw new RuntimeException(e.getMessage());
        }
    }
}
