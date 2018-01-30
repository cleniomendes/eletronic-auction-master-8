package com.taurus.auction.repository;

import com.taurus.auction.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Clenio on 30/01/2018.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {
}
