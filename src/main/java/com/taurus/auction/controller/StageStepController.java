package com.taurus.auction.controller;

/**
 * Created by Clenio on 30/01/2018.
 */

import com.taurus.auction.domain.StageStep;
import com.taurus.auction.repository.StageStepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stageStep")
@PreAuthorize("hasAuthority('Administrador')")
public class StageStepController {
    @Autowired
    private StageStepRepository stageStepRepository;

    private static final Logger log = LoggerFactory.getLogger(StageStepController.class);

    @PostMapping
    @PatchMapping
    public ResponseEntity createStageStep(@RequestBody StageStep stageStep) {
        try {
            stageStepRepository.saveAndFlush(stageStep);
            log.info(String.format("save stageStep=%s success", stageStep.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stageStep);
    }

    @DeleteMapping
    public ResponseEntity deleteStageStep(@RequestBody StageStep stageStep) {
        try {
            stageStepRepository.delete(stageStep);
            log.info(String.format("delete stageStep=%s success", stageStep.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stageStep);
    }

    @GetMapping
    public ResponseEntity listAllStageSteps() {
        List<StageStep> stageSteps = new ArrayList<>();
        try {
            stageSteps = stageStepRepository.findAll();
            if (stageSteps != null) {
                log.info(String.format("Found %s stageSteps", stageSteps.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(stageSteps);
    }
}
