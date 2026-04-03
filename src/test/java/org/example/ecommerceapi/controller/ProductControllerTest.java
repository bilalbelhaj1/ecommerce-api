package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceapi.dto.category.CategorySummaryDTO;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.service.ProductService;
import org.example.ecommerceapi.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    @MockBean
    private RatingService ratingService;

    @Test
    void getProducts() throws Exception{
        List<ProductSummaryDTO> products = List.of(
                new ProductSummaryDTO(1L, "prod1", BigDecimal.TEN, 12, "desc1", new byte[12], 4.5, 12),
                new ProductSummaryDTO(2L, "prod2", BigDecimal.valueOf(20), 12, "desc2", new byte[12], 3.5, 1)
        );
        Page<ProductSummaryDTO> res = new PageImpl<>(products);

        when(productService.getProducts(ProductStatus.ACTIVE, null, 0, 10)).thenReturn(res);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("prod1"))
                .andExpect(jsonPath("$.content[0].price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.content[0].stock").value(12))
                .andExpect(jsonPath("$.content[0].rating").value(4.5))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("prod2"))
                .andExpect(jsonPath("$.content[1].price").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$.content[1].stock").value(12))
                .andExpect(jsonPath("$.content[1].rating").value(3.5));

    }

    @Test
    void searchProducts() throws Exception{
        List<ProductSummaryDTO> products = List.of(
                new ProductSummaryDTO(1L, "prod1", BigDecimal.TEN, 12, "desc1", new byte[12], 4.5, 12),
                new ProductSummaryDTO(2L, "prod2", BigDecimal.valueOf(20), 12, "desc2", new byte[12], 3.5, 1)
        );
        Page<ProductSummaryDTO> res = new PageImpl<>(products);

        when(productService.search("prod1", 0, 10)).thenReturn(res);

        mockMvc.perform(get("/api/v1/products/search").param("q", "prod1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("prod1"))
                .andExpect(jsonPath("$.content[0].price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.content[0].stock").value(12))
                .andExpect(jsonPath("$.content[0].rating").value(4.5))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("prod2"))
                .andExpect(jsonPath("$.content[1].price").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$.content[1].stock").value(12))
                .andExpect(jsonPath("$.content[1].rating").value(3.5));
    }

    @Test
    void getAll() throws Exception{
        List<ProductSummaryDTO> products = List.of(
                new ProductSummaryDTO(1L, "prod1", BigDecimal.TEN, 12, "desc1", new byte[12], 4.5, 12),
                new ProductSummaryDTO(2L, "prod2", BigDecimal.valueOf(20), 12, "desc2", new byte[12], 3.5, 1)
        );
        //Page<ProductSummaryDTO> res = new PageImpl<>(products);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("prod1"))
                .andExpect(jsonPath("$[0].price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$[0].stock").value(12))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("prod2"))
                .andExpect(jsonPath("$[1].price").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$[1].stock").value(12))
                .andExpect(jsonPath("$[1].rating").value(3.5));
    }

    @Test
    void getOne() throws Exception{
        ProductResponseDTO res = new ProductResponseDTO(
                1L,
                "prod1",
                "desc1",
                BigDecimal.TEN,
                12,
                new byte[10],
                "image/png",
                new CategorySummaryDTO("cat1", 1L),
                List.of(new RatingResponseDTO(1L, "cool", 4, new CustomerSummaryDTO(1L, "bil", "bel")))
        );

        when(productService.getProduct(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("prod1"))
                .andExpect(jsonPath("$.price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.stock").value(12))
                .andExpect(jsonPath("$.category.id").value(1L))
                .andExpect(jsonPath("$.ratings.length()").value(1));
    }

    @Test
    void addProduct() throws Exception{
        ProductResponseDTO res = new ProductResponseDTO(
                1L,
                "prod1",
                "desc1",
                BigDecimal.TEN,
                12,
                new byte[10],
                "image/png",
                new CategorySummaryDTO("cat1", 1L),
                List.of(new RatingResponseDTO(1L, "cool", 4, new CustomerSummaryDTO(1L, "bil", "bel")))
        );
        CreateProductDTO req = new CreateProductDTO("prod1", "desc1", BigDecimal.TEN , 12, 1L);
        when(productService.addProduct(null, null)).thenReturn(res);

        mockMvc.perform(post("/api/v1/products").contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(objectMapper.writeValueAsString(req))
                        .content(new byte[10])
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("prod1"))
                .andExpect(jsonPath("$.price").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.stock").value(12))
                .andExpect(jsonPath("$.category.id").value(1L))
                .andExpect(jsonPath("$.ratings.length()").value(1));
    }

    @Test
    void getImageByProductId() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() throws Exception{
        mockMvc.perform(delete("/api/v1/products/{productId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRatings() throws Exception{
        List<RatingResponseDTO> res = List.of(
                new RatingResponseDTO(1L, "cool", 5, new CustomerSummaryDTO(1L, "bil", "bel")),
                new RatingResponseDTO(2L, "bad", 1, new CustomerSummaryDTO(2L, "jon", "doe"))

        );
        when(ratingService.getAll(1L)).thenReturn(res);
        mockMvc.perform(get("/api/v1/products/{id}/ratings", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].customer.id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].rating").value(1))
                .andExpect(jsonPath("$[1].customer.id").value(2L));
    }
}