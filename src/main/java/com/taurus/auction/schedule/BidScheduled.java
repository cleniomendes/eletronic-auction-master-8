package com.taurus.auction.schedule;

import com.taurus.auction.domain.Bid;
import com.taurus.auction.repository.BidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Clenio on 31/01/2018.
 */

@Component
public class BidScheduled {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    private static final Logger log = LoggerFactory.getLogger(BidScheduled.class);

    public void sendAllBids(Long idStageStep, ScheduledExecutorService executor, String currentPrincipalName) {
        List<Bid> bids = bidRepository.findBidByStageStepProduct(idStageStep);

        //If admin isnt logged, shutdown schedule
        if (sessionRegistry.getSessionInformation("taurussession" + currentPrincipalName) == null) {
            log.info(String.format("Shutdown schedule to send all bids by step"));
            executor.shutdown();
        }

        //send to channel
        template.convertAndSend("/topic/all-bids-step-" + idStageStep, bids);
    }
}
