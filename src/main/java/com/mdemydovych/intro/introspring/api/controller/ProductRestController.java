package com.mdemydovych.intro.introspring.api.controller;

import com.mdemydovych.intro.introspring.model.domain.Product;
import com.mdemydovych.intro.introspring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService productService;

  @PostMapping
  public String save(@RequestBody Product product) {
    return productService.save(product);
  }

  @GetMapping
  public Product findById(@RequestParam String id) {
    return productService.findById(id);
  }

  @DeleteMapping
  public void deleteById(@RequestParam String id) {
    productService.deleteById(id);
  }
}
