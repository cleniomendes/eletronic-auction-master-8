package com.taurus.auction.repository;

import com.taurus.auction.domain.Bid;
import com.taurus.auction.domain.StageStepProduct;
import com.taurus.auction.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Clenio on 26/01/2018.
 */

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findBidByUserAndStageStepProduct(User user, StageStepProduct stageStepProduct);

    @Query("SELECT b FROM Bid b " +
            "INNER JOIN b.stageStepProduct stp " +
            "where b.bidStatus in ('Parcialmente atendido','Totalmente atendido') " +
            "and b.bidValue <= :bid_value " +
            "and b.quantityFinal > 0 " +
            "and b.status = 'Ativo'" +
            "and b.stageStepProduct.id = :step_value " +
            "ORDER BY b.bidValue asc")
    List<Bid> findByBidStatusAndBidValueAndQuantityFinal(@Param("bid_value") Double bidValue, @Param("step_value") Long stepValue);

    @Query("SELECT b FROM Bid b " +
            "INNER JOIN b.stageStepProduct stp " +
            "where b.status = 'Ativo' " +
            "and b.stageStepProduct.id = :step_value " +
            "ORDER BY b.bidValue asc")
    List<Bid> findBidByStageStepProductAndStatus(@Param("step_value") Long stepValue);
}
