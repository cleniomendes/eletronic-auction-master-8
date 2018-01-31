package com.taurus.auction.schedule;

import com.taurus.auction.domain.Auction;
import com.taurus.auction.repository.AuctionRepository;
import com.taurus.auction.service.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by Clenio on 18/01/2018.
 */
@Component
public class AuctionScheduled {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionRepository auctionRepository;

    private static final Logger log = LoggerFactory.getLogger(AuctionScheduled.class);

    @Scheduled(fixedDelay = 1000)
    public void clock() {

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();


        template.convertAndSend("/topic/taurus-clock", date);
    }


    public void startAuction(Long id, ScheduledExecutorService e) {
        Auction auction = auctionRepository.findById(id);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d = new Date();

        log.info(String.format("Schedule running at %s for Auction %s", df.format(d), auction.getDescription()));

        if (d.after(auction.getStart_date()) && auction.getStatus().equals("E")) {
            auction.setStatus("A");
            log.info(String.format("Auction %s started", auction.getDescription()));
            auctionRepository.save(auction);
        }

        auction.getStage().forEach(stage -> {
            //If Stage was finish, set and update (CLOSE STAGE)
            if (stage.getStatus().equals("A")) {
                if (d.after(stage.getEnd_date())) {
                    stage.setStatus("F");
                    log.info(String.format("Stage %s ended for auction %s at %s", stage.getDescription(), auction.getDescription(), df.format(d)));
                }
            } else if (stage.getStatus().equals("E")) {
                //If stage start, set status (OPEN STAGE)
                if (d.after(stage.getStart_date())) {
                    stage.setStatus("A");
                    log.info(String.format("Stage %s started for auction %s at %s", stage.getDescription(), auction.getDescription(), df.format(d)));
                }
            }

            stage.getStageStep().forEach(stageStep -> {
                //If Stage was finish, set and update (CLOSE STAGE STEP)
                if (stageStep.getStatus().equals("A")) {
                    if (d.after(stageStep.getEnd_date())) {
                        stageStep.setStatus("F");
                        log.info(String.format("StageStep %s ended for Step %s and Auction %s at %s", stageStep.getDescription(), stage.getDescription(), auction
                                        .getDescription()
                                , df.format
                                        (d)));
                    }

                } else {
                    //If stage start, set status (OPEN STAGE STEP)
                    if (d.after(stageStep.getStart_date()) && d.before(stageStep.getEnd_date())) {
                        stageStep.setStatus("A");
                        log.info(String.format("StageStep %s started for Step %s and Auction %s at %s", stageStep.getDescription(), stage.getDescription
                                        (), auction
                                        .getDescription()
                                , df.format
                                        (d)));
                    }
                }

            });
            auctionRepository.save(auction);
        });

        //If all auctions stages are finished then finish the auction
        int auctionStagesFinished = ((int) auction.getStage().stream().filter(stage -> stage.getStatus().equals("F")).count());
        int allAuctionStages = auction.getStage().size();

        if (allAuctionStages == auctionStagesFinished){
            auction.setStatus("F");
            auctionRepository.save(auction);
            log.info(String.format("Auction %s finished", auction.getDescription()));
            e.shutdown();
        }

    }

    @Scheduled(cron = "0 0 2 * * *")
    public void DailyScheduleStartAuction() throws Exception {
        auctionService.startAuctions();
    }
}
