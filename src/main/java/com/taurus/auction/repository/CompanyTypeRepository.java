package com.taurus.auction.repository;

import com.taurus.auction.domain.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType,Long> {

}
