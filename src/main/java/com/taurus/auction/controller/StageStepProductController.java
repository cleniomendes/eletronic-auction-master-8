package com.taurus.auction.controller;

import com.taurus.auction.domain.StageStepProduct;
import com.taurus.auction.domain.Unit;
import com.taurus.auction.repository.StageStepProductRepository;
import com.taurus.auction.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clenio on 30/01/2018.
 */

@RestController
@RequestMapping("/api/stageStepProduct")
@PreAuthorize("hasAuthority('Administrador')")
public class StageStepProductController {
    @Autowired
    private StageStepProductRepository stageStepProductRepository;

    @Autowired
    private UnitRepository unitRepository;

    private static final Logger log = LoggerFactory.getLogger(StageStepProduct.class);

    @PostMapping
    @PatchMapping
    public ResponseEntity createStageStepProduct(@RequestBody StageStepProduct stageStepProduct) {
        try {
            stageStepProductRepository.saveAndFlush(stageStepProduct);
            log.info(String.format("save stageStepProduct success with product %s", stageStepProduct.getProduct().getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stageStepProduct);
    }

    @DeleteMapping
    public ResponseEntity deleteStageStepProduct(@RequestBody StageStepProduct stageStepProduct) {
        try {
            stageStepProductRepository.delete(stageStepProduct);
            log.info(String.format("delete stageStep %s success", stageStepProduct.getId()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stageStepProduct);
    }

    @GetMapping(value = "/units")
    public ResponseEntity listAllUnits() {
        List<Unit> units = new ArrayList<>();
        try {
            units = unitRepository.findAll();
            if (units != null) {
                log.info(String.format("Found %s units", units.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(units);
    }
}
