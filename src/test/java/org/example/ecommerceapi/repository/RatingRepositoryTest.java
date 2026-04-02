package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class RatingRepositoryTest {
    @Autowired
    private RatingRepository underTest;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private Customer customer;
    @Autowired
    private RatingRepository ratingRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder()
                .address("tetouan")
                .firstName("bilal")
                .lastName("belhaj")
                .email("bilal@gmail.comm")
                .appUser(AppUser.builder().password("password").username("bilal@gmail.com").build())
                .build());

        Category cat = categoryRepository.save(Category.builder()
                .name("cat")
                .description("des")
                .build()
        );
        product = productRepository.save(Product.builder()
                .name("produc")
                .description("des")
                .imageType("png")
                .imageData(new byte[1])
                .stock(12)
                .price(BigDecimal.TEN)
                .status(ProductStatus.ACTIVE)
                .category(cat)
                .build());
    }

    @Test
    void findAllByProduct_returnsProducts_whenExists() {
        // given
        Rating rating = Rating.builder().product(product).rating(4).comment("something").customer(customer).build();
        ratingRepository.save(rating);
        // when
        List<Rating> res = underTest.findAllByProduct(product);
        // then
        assertThat(res).isNotEmpty();
    }

    @Test
    void findAllByProduct_returnsEmpty_whenNotExists() {

        List<Rating> res = underTest.findAllByProduct(product);

        assertThat(res).isEmpty();
    }
}