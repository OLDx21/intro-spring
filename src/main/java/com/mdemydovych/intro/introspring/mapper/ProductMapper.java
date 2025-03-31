package com.mdemydovych.intro.introspring.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.mdemydovych.intro.introspring.model.domain.Product;
import com.mdemydovych.intro.introspring.model.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

  Product entityToDomain(ProductEntity product);

  ProductEntity domainToEntity(Product product);
}
