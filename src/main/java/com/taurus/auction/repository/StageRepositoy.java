package com.taurus.auction.repository;

import com.taurus.auction.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Clenio on 30/01/2018.
 */
@Repository
public interface StageRepositoy extends JpaRepository<Stage,Long>{
}
