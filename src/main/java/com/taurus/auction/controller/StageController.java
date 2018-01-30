package com.taurus.auction.controller;

import com.taurus.auction.domain.Stage;
import com.taurus.auction.repository.StageRepositoy;
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
@RequestMapping("/api/stage")
@PreAuthorize("hasAuthority('Administrador')")
public class StageController {

    @Autowired
    private StageRepositoy stageRepositoy;

    private static final Logger log = LoggerFactory.getLogger(StageController.class);

    @PostMapping
    @PatchMapping
    public ResponseEntity createStage(@RequestBody Stage stage) {
        try {
            stageRepositoy.saveAndFlush(stage);
            log.info(String.format("save stage=%s success", stage.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stage);
    }

    @DeleteMapping
    public ResponseEntity deleteStage(@RequestBody Stage stage) {
        try {
            stageRepositoy.delete(stage);
            log.info(String.format("delete stage=%s success", stage.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(stage);
    }

    @GetMapping
    public ResponseEntity listAllStages() {
        List<Stage> stages = new ArrayList<>();
        try {
            stages = stageRepositoy.findAll();
            if (stages != null) {
                log.info(String.format("Found %s stages", stages.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(stages);
    }
}
