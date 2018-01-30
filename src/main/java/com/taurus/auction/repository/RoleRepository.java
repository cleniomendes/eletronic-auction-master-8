package com.taurus.auction.repository;

import com.taurus.auction.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Clenio on 29/01/2018.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
}
