package com.taurus.auction.repository;

import com.taurus.auction.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Clenio on 29/01/2018.
 */
public interface ProductRepository extends JpaRepository<Product,Long>{
}
