package org.example.ecommerceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceapi.dto.customer.CustomerSummaryDTO;
import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author $(bilal belhaj)
 **/
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "CUSTOMER")
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RatingService ratingService;

    @Test
    void rateProduct() throws Exception {
        CreateRatingDTO req = new CreateRatingDTO(1L, "cool", 4, 1L);
        RatingResponseDTO res = new RatingResponseDTO(1L, "cool", 4, new CustomerSummaryDTO(1L, "bil", "bel"));

        when(ratingService.create(req)).thenReturn(res);

        mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.comment").value(res.comment()))
                .andExpect(jsonPath("$.rating").value(res.rating()))
                .andExpect(jsonPath("$.customer.id").value(res.customer().id()));
    }

    @Test
    void getRatings() throws Exception{
        List<RatingResponseDTO> res = List.of(
                new RatingResponseDTO(1L, "cool", 4, new CustomerSummaryDTO(1L, "bil", "bel")),
                new RatingResponseDTO(2L, "bad", 1, new CustomerSummaryDTO(2L, "jon", "doe"))
        );

        when(ratingService.getAll(1L)).thenReturn(res);

        mockMvc.perform(get("/api/v1/ratings/product/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(res.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].comment").value("cool"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[0].customer.id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].comment").value("bad"))
                .andExpect(jsonPath("$[1].rating").value(1))
                .andExpect(jsonPath("$[1].customer.id").value(2L));

    }
}