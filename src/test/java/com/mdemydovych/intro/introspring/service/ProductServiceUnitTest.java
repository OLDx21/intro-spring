package com.mdemydovych.intro.introspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mdemydovych.intro.introspring.exception.ProductNotFoundException;
import com.mdemydovych.intro.introspring.mapper.ProductMapper;
import com.mdemydovych.intro.introspring.model.domain.Product;
import com.mdemydovych.intro.introspring.model.entity.ProductEntity;
import com.mdemydovych.intro.introspring.repository.ProductRepository;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.NamedExecutable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class ProductServiceUnitTest {

  @MockitoBean
  private ProductRepository repository;

  @Autowired
  private ProductMapper mapper;

  @Autowired
  private ProductService productService;

  @Test
  @WithMockUser(authorities = "CHIEF")
  void shouldSaveProduct() {
    Product product = Instancio.create(Product.class);
    when(repository.save(any())).thenReturn(mapper.domainToEntity(product));
    productService.save(product);
    verify(repository).save(any());
  }

  @Test
  @WithMockUser(authorities = "EMPLOYEE")
  void shouldCorrectFindProduct() {
    ProductEntity entity = Instancio.create(ProductEntity.class);
    when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
    Product result = productService.findById(entity.getId());
    assertEquals(entity.getId(), result.getId());
  }

  @Test
  @WithMockUser(authorities = "EMPLOYEE")
  void shouldThrowExceptionWhenProductNotFound() {
    when(repository.findById(any())).thenReturn(Optional.empty());
    Assertions.assertThrows(ProductNotFoundException.class,
        (NamedExecutable) () -> productService.findById("123"));
  }

  @Test
  @WithMockUser(authorities = "CHIEF")
  void shouldThrowExceptionWhenUserHasInsufficientAuthorityFindById() {
    Assertions.assertThrows(AuthorizationDeniedException.class,
        (NamedExecutable) () -> productService.findById("123"));
  }

  @Test
  @WithMockUser(authorities = "EMPLOYEE")
  void shouldThrowExceptionWhenUserHasInsufficientAuthoritySave() {
    Assertions.assertThrows(AuthorizationDeniedException.class,
        (NamedExecutable) () -> productService.save(null));
  }
}
