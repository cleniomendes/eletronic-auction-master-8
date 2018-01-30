package com.taurus.auction.service;

import com.taurus.auction.domain.StageStepProduct;
import com.taurus.auction.repository.StageStepProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Clenio on 26/01/2018.
 */
@Service
public class StageStepProductService {

    @Autowired
    StageStepProductRepository stageStepProductRepository;

    public StageStepProduct findStageStepProductById(Long id) {return (StageStepProduct) stageStepProductRepository.findById(id);}
}
