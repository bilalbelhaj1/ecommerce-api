package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void createCategory() throws Exception{
        CreateCategoryDTO req = new CreateCategoryDTO("cat", "desc");
        CategoryResponseDTO res = new CategoryResponseDTO(1L, "cat", "desc", 10);
        when(categoryService.addCategory(req)).thenReturn(res);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.name").value(res.name()))
                .andExpect(jsonPath("$.description").value(res.description()));
    }

    @Test
    void getCategories() throws Exception{
        List<CategoryResponseDTO> res = List.of(
                new CategoryResponseDTO(1L, "cat1", "desc1", 10),
                new CategoryResponseDTO(2L, "cat2", "desc2", 4)
        );
        when(categoryService.getCategories()).thenReturn(res);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(res.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("cat1"))
                .andExpect(jsonPath("$[0].description").value("desc1"))
                .andExpect(jsonPath("$[0].productsNbr").value(10))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getCategory() throws Exception{
        CategoryResponseDTO res = new CategoryResponseDTO(1L, "cat1", "desc1", 10);

        when(categoryService.getCategory(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/categories/{categoryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.name").value(res.name()))
                .andExpect(jsonPath("$.description").value(res.description()))
                .andExpect(jsonPath("$.productsNbr").value(res.productsNbr()));
    }

    @Test
    void updateCategory() throws Exception {
        UpdateCategoryDTO req = new UpdateCategoryDTO("catUpdate", "null");
        CategoryResponseDTO res = new CategoryResponseDTO(1L, "catUpdate", "desc1", 4);

        when(categoryService.updateCategory(1L, req)).thenReturn(res);

        mockMvc.perform(put("/api/v1/categories/{categoryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.name").value(res.name()))
                .andExpect(jsonPath("$.description").value(res.description()))
                .andExpect(jsonPath("$.productsNbr").value(res.productsNbr()));
    }

    @Test
    void deleteCategory() throws Exception{
        mockMvc.perform(delete("/api/v1/categories/{categoryId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProducts() throws Exception{
        List<ProductSummaryDTO> res = List.of(
                new ProductSummaryDTO(1L, "prod1", BigDecimal.TEN, 10, "desc1", new byte[12], 4.5,12),
                new ProductSummaryDTO(2L, "prod2", BigDecimal.valueOf(20), 20, "desc2", new byte[12], 4.5,10)
        );

        when(categoryService.getProductsByCategory(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/categories/{categoryId}/products", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(res.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("prod1"))
                .andExpect(jsonPath("$[0].price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].nbrRatings").value(12))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("prod2"))
                .andExpect(jsonPath("$[1].price").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$[1].stock").value(20));
    }
}