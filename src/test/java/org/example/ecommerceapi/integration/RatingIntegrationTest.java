package org.example.ecommerceapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RatingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder()
                .firstName("bilal")
                .lastName("belhaj")
                .email("bilal@gmail.com")
                .address("tetouan")
                .appUser(AppUser.builder().username("bilal@gmail.com").password("password").build())
                .build());

        Category cat = categoryRepository.save(Category.builder().name("cat").description("desc").build());

        product = productRepository.save(
                Product.builder()
                        .name("prod")
                        .description("desc")
                        .stock(12)
                        .price(BigDecimal.TEN)
                        .imageType("png")
                        .imageData(new byte[3])
                        .category(cat)
                        .build()
        );
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createRating_success() throws Exception {

        CreateRatingDTO req = new CreateRatingDTO(product.getId(), "cool", 4,customer.getId());
        mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value(req.comment()))
                .andExpect(jsonPath("$.rating").value(req.rating()))
                .andExpect(jsonPath("$.customer.id").value(customer.getId()));
    }

    @Test
    void getRatings_success() throws Exception {
        Rating rating1 = Rating.builder().customer(customer).product(product).rating(4).comment("cool").build();
        Rating rating2 = Rating.builder().customer(customer).product(product).rating(1).comment("bad").build();

        List<Rating> saved = ratingRepository.saveAll(List.of(rating1, rating2));

        mockMvc.perform(get("/api/v1/ratings/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(saved.size()))
                .andExpect(jsonPath("$[0].id").value(saved.get(0).getId()))
                .andExpect(jsonPath("$[0].comment").value(saved.get(0).getComment()))
                .andExpect(jsonPath("$[0].rating").value(saved.get(0).getRating()))
                .andExpect(jsonPath("$[1].id").value(saved.get(1).getId()))
                .andExpect(jsonPath("$[1].comment").value(saved.get(1).getComment()))
                .andExpect(jsonPath("$[1].rating").value(saved.get(1).getRating()));

    }
}
