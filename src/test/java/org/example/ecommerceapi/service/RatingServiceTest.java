package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.rating.CreateRatingDTO;
import org.example.ecommerceapi.dto.rating.RatingResponseDTO;
import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CustomerRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.repository.RatingRepository;
import org.example.ecommerceapi.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {
    @Mock private RatingRepository ratingRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private RatingServiceImpl underTest;
    private Customer customer;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .email("bilal@gmail.com")
                .firstName("bilal")
                .lastName("belhaj")
                .address("tetouan")
                .appUser(AppUser.builder().username("bilal@gmail.com").password("password").build())
                .build();

        category = Category.builder().name("cat").description("desc").build();

        product = Product.builder()
                .name("prod")
                .description("des")
                .status(ProductStatus.ACTIVE)
                .stock(12)
                .price(BigDecimal.TEN)
                .imageData(new byte[12])
                .imageType("png")
                .category(category)
                .build();
    }

    @Test
    void canCreateRating_whenCustomerAndProductExist(){
        // given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        // when
        RatingResponseDTO res = underTest.create(new CreateRatingDTO(1L, "foo", 4, 1L));

        ArgumentCaptor<Rating> ratingArgumentCaptor = ArgumentCaptor.forClass(Rating.class);
        // then
        verify(ratingRepository).save(ratingArgumentCaptor.capture());
        Rating saved = ratingArgumentCaptor.getValue();
        assertThat(saved.getId()).isEqualTo(res.id());
    }

    @Test
    void createRating_throws_whenCustomerNotFound(){
        // given
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(
                () -> underTest.create(new CreateRatingDTO(1L, "foo", 4, 1L))
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createRating_throws_whenProductNotFound(){
        // given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(
                () -> underTest.create(new CreateRatingDTO(1L, "foo", 4, 1L))
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canGetAllRatings_forProduct(){
        // given
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(ratingRepository.findAllByProduct(product)).thenReturn(
                List.of(
                        Rating.builder().rating(4).comment("foo").customer(customer).product(product).build(),
                        Rating.builder().rating(4).comment("boo").customer(customer).product(product).build()
                )
        );
        // when
        List<RatingResponseDTO> res = underTest.getAll(1L);
        // then
        assertThat(res).isNotEmpty();
        assertThat(res.size()).isEqualTo(2);
    }

    @Test
    void getAllRatings_throws_whenProductNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAll(1L)).isInstanceOf(ResourceNotFoundException.class);
    }
}