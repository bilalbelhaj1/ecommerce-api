package org.example.ecommerceapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_success() throws Exception{
        CreateCategoryDTO req = new CreateCategoryDTO("cat1", "desc1");

        //Category cat = categoryRepository.save(Category.builder().name("cat1").description("desc1").build());

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(req.name()))
                .andExpect(jsonPath("$.description").value(req.description()));
    }

    @Test
    void getCategories_returnsAll() throws Exception{
        List<Category> cats = List.of(
                Category.builder().name("cat1").description("desc1").build(),
                Category.builder().name("cat2").description("desc2").build()
        );
        categoryRepository.saveAll(cats);

        List<Category> saved = categoryRepository.findAll();

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(saved.size()))
                .andExpect(jsonPath("$[0].id").value(saved.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(saved.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(saved.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(saved.get(1).getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryById_success() throws Exception {
        Category saved = categoryRepository.save(Category.builder().name("cat1").description("desc1").build());

        mockMvc.perform(get("/api/v1/categories/{categorieId}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.productsNbr").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryById_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_success() throws Exception {
        Category cat = Category.builder().name("cat").description("desc").build();
        Category saved = categoryRepository.save(cat);
        UpdateCategoryDTO dto = new UpdateCategoryDTO("catUpdate", null);

        mockMvc.perform(put("/api/v1/categories/{categoryId}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.description").value(saved.getDescription()));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_withDescription_success() throws Exception {
        Category cat = Category.builder().name("cat").description("desc").build();
        Category saved = categoryRepository.save(cat);
        UpdateCategoryDTO dto = new UpdateCategoryDTO("catUpdate", "descUpdate");

        mockMvc.perform(put("/api/v1/categories/{categoryId}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.description").value(dto.description()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_success() throws Exception{
        Category saved = categoryRepository.save(Category.builder().name("cat").description("desc").build());

        mockMvc.perform(delete("/api/v1/categories/{categoryId}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProductsByCategory_success() throws Exception {
        Category saved = categoryRepository.save(Category.builder().name("cat1").description("desc").build());
        List<Product> products = List.of(
                Product.builder().name("prod1").description("desc1").stock(10).price(BigDecimal.TEN).category(saved).build(),
                Product.builder().name("prod2").description("desc2").stock(20).price(BigDecimal.valueOf(20)).category(saved).build()
        );
        List<Product> savedProducts = productRepository.saveAll(products);

        mockMvc.perform(get("/api/v1/categories/{categoryId}/products", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(savedProducts.size()))
                .andExpect(jsonPath("$[0].id").value(savedProducts.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(savedProducts.get(0).getName()))
                .andExpect(jsonPath("$[0].description").value(savedProducts.get(0).getDescription()))
                .andExpect(jsonPath("$[0].stock").value(savedProducts.get(0).getStock()))
                .andExpect(jsonPath("$[1].id").value(savedProducts.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(savedProducts.get(1).getName()))
                .andExpect(jsonPath("$[1].description").value(savedProducts.get(1).getDescription()))
                .andExpect(jsonPath("$[1].stock").value(savedProducts.get(1).getStock()));
    }
}
