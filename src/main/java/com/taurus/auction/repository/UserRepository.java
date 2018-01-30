package com.taurus.auction.repository;

import com.taurus.auction.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Clenio on 19/01/18.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
