package com.mdemydovych.intro.introspring.it;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdemydovych.intro.introspring.TestcontainersConfiguration;
import com.mdemydovych.intro.introspring.model.domain.Product;
import com.mdemydovych.intro.introspring.repository.ProductRepository;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestControllerItTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private ProductRepository repository;

  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  public void tearDown() {
    repository.deleteAll();
  }

  @Test
  @SneakyThrows
  @WithMockUser(authorities = "CHIEF")
  void shouldSaveProduct() {
    var request = generateProduct();
    MvcResult result = mockMvc.perform(post("/product")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    assertEquals(result.getResponse().getContentAsString(), repository.findAll().get(0).getId());
  }

  @Test
  @SneakyThrows
  @WithMockUser(authorities = "EMPLOYEE")
  void shouldReturnForbidden() {
    var request = generateProduct();
    mockMvc.perform(post("/product")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockUser(authorities = {"EMPLOYEE", "CHIEF"})
  void shouldCorrectlySaveAndGet() {
    var request = generateProduct();
    MvcResult saveResult = mockMvc.perform(post("/product")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    String id = saveResult.getResponse().getContentAsString();

    MvcResult result = mockMvc.perform(get("/product?id=" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    Product product = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
    assertEquals(id, product.getId());
  }

  @Test
  @SneakyThrows
  @WithMockUser(authorities = {"EMPLOYEE"})
  void shouldReturnNotFound() {
    mockMvc.perform(get("/product?id=" + "123")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  private Product generateProduct() {
    return Instancio.of(Product.class).ignore(field(Product::getId)).create();
  }
}
