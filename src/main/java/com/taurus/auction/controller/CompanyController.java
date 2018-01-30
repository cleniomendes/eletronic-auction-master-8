package com.taurus.auction.controller;

import com.taurus.auction.domain.Company;
import com.taurus.auction.domain.CompanyType;
import com.taurus.auction.repository.CompanyRepository;
import com.taurus.auction.repository.CompanyTypeRepository;
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
@RequestMapping("/api/company")
@PreAuthorize("hasAuthority('Administrador')")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @PostMapping
    @PatchMapping
    public ResponseEntity createCompany(@RequestBody Company company) {
        try {
            companyRepository.saveAndFlush(company);
            log.info(String.format("save company=%s success", company.getName()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @DeleteMapping
    public ResponseEntity deleteCompany(@RequestBody Company company) {
        try {
            companyRepository.delete(company);
            log.info(String.format("delete company=%s success", company.getName()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping
    public ResponseEntity listAllCompany() {
        List<Company> companyList = new ArrayList<>();
        try {
            companyList = companyRepository.findAll();
            if (companyList != null) {
                log.info(String.format("Found %s companies type", companyList.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.ok().body(companyList);
    }

    @GetMapping(value = "/companyType")
    public ResponseEntity listAllCompanyType() {
        List<CompanyType> companyTypeList = new ArrayList<>();
        try {
            companyTypeList = companyTypeRepository.findAll();
            if (companyTypeList != null) {
                log.info(String.format("Found %s companies type", companyTypeList.size()));
            }

        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.ok().body(companyTypeList);
    }

    @PostMapping(value = "/companyType")
    @PatchMapping(value = "/companyType")
    public ResponseEntity createCompanyType(@RequestBody CompanyType companyType) {
        try {
            companyTypeRepository.saveAndFlush(companyType);
            log.info(String.format("save companyType=%s success", companyType.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(companyType);
    }

    @DeleteMapping(value = "/companyType")
    public ResponseEntity deleteCompanyType(@RequestBody CompanyType companyType) {
        try {
            companyTypeRepository.delete(companyType);
            log.info(String.format("delete companyType=%s success", companyType.getDescription()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(companyType);
    }

}
