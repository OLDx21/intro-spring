package com.mdemydovych.intro.introspring.service;

import com.mdemydovych.intro.introspring.exception.ProductNotFoundException;
import com.mdemydovych.intro.introspring.mapper.ProductMapper;
import com.mdemydovych.intro.introspring.model.domain.Product;
import com.mdemydovych.intro.introspring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repository;

  private final ProductMapper mapper;

  @PreAuthorize("hasAuthority('CHIEF')")
  public String save(Product domain) {
    return repository.save(mapper.domainToEntity(domain)).getId();
  }

  @PreAuthorize("hasAuthority('EMPLOYEE')")
  public Product findById(String id) {
    return mapper.entityToDomain(
        repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
  }

  @PreAuthorize("hasAuthority('CHIEF')")
  public void deleteById(String id) {
    repository.deleteById(id);
  }
}
