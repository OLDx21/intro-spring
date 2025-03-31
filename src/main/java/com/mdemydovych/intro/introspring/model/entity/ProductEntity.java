package com.mdemydovych.intro.introspring.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity(name = "products")
public class ProductEntity {

  @Id
  @UuidGenerator
  private String id;

  private String name;

  private int count;

  private String description;

}
