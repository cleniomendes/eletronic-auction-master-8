package com.taurus.auction.business;

import com.taurus.auction.domain.*;
import com.taurus.auction.domain.StageStepProduct;
import com.taurus.auction.repository.BidRepository;
import com.taurus.auction.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clenio on 26/01/2018.
 */
@Service
public class BidBusiness {
    private static final Logger log = LoggerFactory.getLogger(BidBusiness.class);

    @Autowired
    StageStepProductService stageStepProductService;

    @Autowired
    UserService userService;

    @Autowired
    BidService bidService;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private List<Bid> setChangedStatus;

    public Bid createBid(String username, Long idStageStepProduct, Long quantity, Double bidValue) {
        setChangedStatus = new ArrayList<>();
        Bid bid = new Bid();
        //Find and set user
        User user = userService.findUserByUsername(username);
        bid.setUser(user);
        //Find and set StageStep
        StageStepProduct stageStepProduct = stageStepProductService.findStageStepProductById(idStageStepProduct);
        bid.setStageStepProdut(stageStepProduct);
        //Set other values
        bid.setQuantity(quantity);
        bid.setBidValue(bidValue);
        bid.setQuantityFinal(0L);

        log.info(String.format("Creating bid for user %s", username));

        this.checkValuesMinAndReservation(bid);

        //After check all conditons
        //Call function to disable my last bid
        List<Bid> bidTodisable = bidService.findBidByUserAndStageStepProduct(user, stageStepProduct);
        if (!bidTodisable.isEmpty()) {
            if (bidTodisable.size() > 1) {
                bidTodisable.forEach(bid1 -> {
                    bid1.setStatus("Desativado");
                    //Devolver o resto
                    if (bid1.getQuantityFinal() != 0L) {
                        bid1.getStageStepProdut().getProduct().setAvailabilityQuantity(bid1.getQuantityFinal() + bid.getStageStepProdut()
                                .getProduct().getAvailabilityQuantity());
                        bid1.setQuantityFinal(0L);
                        this.verifyStatus(bid1);
                    }
                    bidRepository.save(bid1);
                });
            } else {
                bidTodisable.get(0).setStatus("Desativado");
                //Devolver o resto
                if (bidTodisable.get(0).getQuantityFinal() != 0L) {
                    bidTodisable.get(0).getStageStepProdut().getProduct().setAvailabilityQuantity(bidTodisable.get(0).getQuantityFinal() + bid.getStageStepProdut()
                            .getProduct().getAvailabilityQuantity());
                    bidTodisable.get(0).setQuantityFinal(0L);
                    this.verifyStatus(bidTodisable.get(0));
                }
                bidRepository.save(bidTodisable);
            }
        }

        setChangedStatus.add(bid);

        //call save bid and update status
        this.SaveBid(setChangedStatus);

        return bid;
    }

    public Bid checkValuesMinAndReservation(Bid bid) {
        log.info(String.format("Check values min and reservation preice for user %s", bid.getUser().getUsername()));
        Double bidValue = bid.getBidValue();
        Long minValue = bid.getStageStepProdut().getProduct().getMin_price();
        Long reservationValue = bid.getStageStepProdut().getProduct().getReservation_price();

        if (bidValue >= minValue && bidValue >= reservationValue) {
            this.checkAvailability(bid);
        } else {
//            bid.setBidstatus("Não atendido");
            //Finish round and set quantity final
            bid.setQuantityFinal(0L);
//            bid.setStatus("Ativo");
        }
        return bid;
    }

    public Boolean checkQuantityLastro(Bid bid) {
        log.info(String.format("Check quantity lastro for user %s", bid.getUser().getUsername()));

        Long lastro = bid.getUser().getLastro();
        Long quantityBid = bid.getQuantity();
        if (quantityBid >= lastro) {
            return true;
        }
        return false;
    }

    public Bid checkAvailability(Bid bid) {
        log.info(String.format("Check availability product"));

        Long availabilityQuantity = bid.getStageStepProdut().getProduct().getAvailabilityQuantity();
        Long quantityBid = bid.getQuantity();
        Long lastro = bid.getUser().getLastro();
        if (availabilityQuantity > 0) {
            if (quantityBid > availabilityQuantity) {
                if (this.checkQuantityLastro(bid)){
                    bid.setQuantityFinal(lastro);
                    bid.getStageStepProdut().getProduct().setAvailabilityQuantity(availabilityQuantity - lastro);
                }else{
                    bid.setQuantityFinal(availabilityQuantity);
                    bid.getStageStepProdut().getProduct().setAvailabilityQuantity(0L);
                    this.goFight(bid);
                }
            } else {
                if (this.checkQuantityLastro(bid)) {
                    //Finish round and set quantity final
                    bid.setQuantityFinal(lastro);
//                    bid.setStatus("Ativo");
                    bid.getStageStepProdut().getProduct().setAvailabilityQuantity(availabilityQuantity - lastro);
                } else {
//                    bid.setBidstatus("Totalmente atendido");
                    //Finish round and set quantity final
                    bid.setQuantityFinal(quantityBid);
//                    bid.setStatus("Ativo");
                    bid.getStageStepProdut().getProduct().setAvailabilityQuantity(availabilityQuantity - quantityBid);
                }
            }
        } else {
            this.goFight(bid);
        }
        return bid;
    }

    public Bid goFight(Bid bid) {
        log.info(String.format("User %s go to fight", bid.getUser().getUsername()));

        List<Bid> selectedBids = bidService.findByBidStatusAndBidValueAndQuantityFinal(bid.getBidValue(), bid.getStageStepProdut().getId());
        Long qtdOld = 0L;

        if (!selectedBids.isEmpty() && bid.getQuantityFinal() != bid.getQuantity()) {
            Bid bidToSteal = selectedBids.get(0);
            if (this.checkQuantityLastro(bid)) {
                qtdOld = bid.getQuantity();
                bid.setQuantity(bid.getUser().getLastro());
            }

            if (bid.getQuantity() <= bidToSteal.getQuantityFinal()) {
                this.stealQuantity(bid, bidToSteal);
            } else {
                this.stealQuantity(bid, bidToSteal);
                this.goFight(bid);
            }
        }

        if (qtdOld != 0L) {
            bid.setQuantity(qtdOld);
        }

        return bid;
    }

    public Bid stealQuantity(Bid bid, Bid stolenBid) {
        log.info(String.format("User %s steal quantity", bid.getUser().getUsername()));
        Long quantityToSteal = bid.getQuantity() - bid.getQuantityFinal();

        if (quantityToSteal >= stolenBid.getQuantityFinal()) {
            bid.setQuantityFinal(bid.getQuantityFinal() + stolenBid.getQuantityFinal());
            stolenBid.setQuantityFinal(0L);
        } else {
            stolenBid.setQuantityFinal(stolenBid.getQuantityFinal() - quantityToSteal);
            bid.setQuantityFinal(bid.getQuantityFinal() + quantityToSteal);

            if (stolenBid.getUser().getUsername().equals(bid.getUser().getUsername()) && stolenBid.getQuantityFinal() != 0L) {
                bid.getStageStepProdut().getProduct().setAvailabilityQuantity(stolenBid.getQuantityFinal());
                stolenBid.setQuantityFinal(0L);
            }
        }

        setChangedStatus.add(stolenBid);
        this.SaveBid(setChangedStatus);
        setChangedStatus.clear();
        return bid;
    }

    public void SaveBid(List<Bid> bids) {
        if (bids.size() > 1) {
            bids.forEach(bid -> {
                this.verifyStatus(bid);
                bidRepository.save(bid);
            });
        } else {
            Bid bid = this.verifyStatus(bids.get(0));
            bidRepository.save(bid);
        }
    }

    public Bid verifyStatus(Bid bid) {
        log.info(String.format("User %s save bid", bid.getUser().getUsername()));

        if (bid.getQuantityFinal() == 0L) {
            bid.setBidstatus("Não atendido");
        } else if (bid.getQuantityFinal() == bid.getQuantity()) {
            bid.setBidstatus("Totalmente atendido");
        } else {
            bid.setBidstatus("Parcialmente atendido");
        }

        if (bid.getStatus() == null) {
            bid.setStatus("Ativo");
        }

        return bid;
    }

    public void sendUpdatedStatus(Long id) throws Exception {
        List<Bid> b = bidService.findBidByStageStepProductAndStatus(id);

        if (b.size() > 1) {
            b.forEach(bid -> {
                try {
                    this.sendStatusToUser(bid);
                } catch (Exception e) {
                    e.getMessage();
                }
            });
        } else {
            this.sendStatusToUser(b.get(0));
        }
    }

    public void sendStatusToUser(Bid bid) throws Exception {
        String status = bid.getBidstatus();
        messagingTemplate.convertAndSendToUser(bid.getUser().getUsername(), "/queue/reply", status);
        log.info(String.format("Sent to channel %s", bid.getUser().getUsername()));
    }
}
