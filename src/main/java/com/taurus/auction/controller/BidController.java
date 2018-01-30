package com.taurus.auction.controller;


import com.taurus.auction.business.BidBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Clenio on 25/01/2018.
 */
@RestController
@RequestMapping("/api/bid")
public class BidController {

    @Autowired
    private BidBusiness bidBusiness;

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
}
