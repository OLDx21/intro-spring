package com.mdemydovych.intro.introspring.repository;

import com.mdemydovych.intro.introspring.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

}
