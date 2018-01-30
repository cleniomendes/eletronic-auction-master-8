package com.taurus.auction.service;

import com.taurus.auction.domain.Bid;
import com.taurus.auction.domain.StageStepProduct;
import com.taurus.auction.domain.User;
import com.taurus.auction.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Clenio on 26/01/2018.
 */

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    public List<Bid> findBidByUserAndStageStepProduct(User user, StageStepProduct stageStepProduct){ return (List<Bid>) bidRepository
            .findBidByUserAndStageStepProduct(user,stageStepProduct);}

    public List<Bid> findByBidStatusAndBidValueAndQuantityFinal(Double bidValue, Long stageStep) {return (List<Bid>) bidRepository
            .findByBidStatusAndBidValueAndQuantityFinal(bidValue,stageStep);}


    public List<Bid> findBidByStageStepProductAndStatus(Long idStageStepProduct) {return (List<Bid>) bidRepository
            .findBidByStageStepProductAndStatus(idStageStepProduct);}
}
