package com.taurus.auction.controller;


import com.taurus.auction.business.BidBusiness;
import com.taurus.auction.schedule.BidScheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Clenio on 25/01/2018.
 */
@RestController
@RequestMapping("/api/bid")
public class BidController {

    @Autowired
    private BidBusiness bidBusiness;

    @Autowired
    private BidScheduled bidScheduled;

    private static final Logger log = LoggerFactory.getLogger(BidController.class);

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public void monitorBid(@RequestBody Map<String,Object> requestJson) throws Exception {
        String username = requestJson.get("username").toString();
        Long idStageStepProduct = Long.parseLong(requestJson.get("idStageStepProduct").toString());
        Long quantity = Long.parseLong(requestJson.get("quantity").toString());
        Double bidValue = Double.parseDouble(requestJson.get("bidValue").toString());

        bidBusiness.createBid(username,idStageStepProduct,quantity,bidValue);

        //after save new bid, send for all users new status
        bidBusiness.sendUpdatedStatus(idStageStepProduct);
    }

    @GetMapping(value = "/list/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    public void getAllLoggedUsers(@PathVariable("id") Long idStageStep) {
        //Get logged user for this request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentPrincipalName = authentication.getName();

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

            log.info(String.format("Start schedule to send Logged Users"));
            Runnable task = () -> bidScheduled.sendAllBids(idStageStep, executor, currentPrincipalName);
            executor.scheduleAtFixedRate(task, 2, 5, TimeUnit.SECONDS);
        }
    }
}
