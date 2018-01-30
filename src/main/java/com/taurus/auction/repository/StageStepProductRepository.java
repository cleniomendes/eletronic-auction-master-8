package com.taurus.auction.repository;

import com.taurus.auction.domain.StageStepProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Clenio on 26/01/2018.
 */

@Repository
public interface StageStepProductRepository extends JpaRepository<StageStepProduct,Long> {
    StageStepProduct findById(Long id);

}
