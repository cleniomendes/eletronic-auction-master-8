package com.taurus.auction.schedule;

import com.taurus.auction.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Clenio on 31/01/2018.
 */

@Component
public class UserScheduled {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SessionRegistry sessionRegistry;

    private static final Logger log = LoggerFactory.getLogger(UserScheduled.class);

    public void sendLoggedUsers(Long idAuction, ScheduledExecutorService executor, String currentPrincipalName) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<String> usersNamesList = new ArrayList<>();

        principals.forEach(o -> {
            if (o instanceof User && !((User) o).getAuctionUsers().isEmpty()) {
                if (((User) o).getAuctionUsers().stream().filter(auctionUser -> auctionUser.getAuction().getId() == idAuction && auctionUser
                        .getStatus().equals("A"))
                        .count() > 0L) {
                    usersNamesList.add(((User) o).getUsername());
                }
            }
        });

        //If admin isnt logged, shutdown schedule
        if (sessionRegistry.getSessionInformation("taurussession" + currentPrincipalName) == null) {
            log.info(String.format("Shutdown schedule to send Logged Users"));
            executor.shutdown();
        }

        //send to channel
        template.convertAndSend("/topic/all-logged-auction-" + idAuction, usersNamesList);
    }
}
