package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.*;
import org.example.ecommerceapi.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository underTest;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired CustomerRepository customerRepository;

    private Category category;
    private Product product;
    private Customer customer;
    private final String name = "product1";
    private final String des = "des";
    @BeforeEach
    void setUp() {
        category = categoryRepository.save(Category.builder()
                .name("cat1")
                .description("category description")
                .build());
        product = Product.builder()
                .name(name)
                .price(BigDecimal.valueOf(234.1))
                .stock(12)
                .description(des)
                .status(ProductStatus.ACTIVE)
                .category(category)
                .imageData(new byte[10])
                .imageType("png")
                .build();
        customer = customerRepository.save(Customer.builder()
                .address("tetouan")
                .firstName("bilal")
                .lastName("belhaj")
                .email("bilal@gmail.comm")
                .appUser(AppUser.builder().password("password").username("bilal@gmail.com").build())
                .build());
    }

    @Test
    void rating_returnsZero_whenNoRatings() {
        // given
        Product saved = underTest.save(product);
        // when
        double res = underTest.rating(saved.getId());
        // then
        assertThat(res).isZero();
    }

    @Test
    void rating_returnsCorrectRating_whenRatings() {
        // given
        product = underTest.save(product);
        Rating rating1 = generateRating("this is cool", 4);
        Rating rating2 = generateRating("this is great", 5);
        ratingRepository.saveAll(List.of(rating1, rating2));
        // when
        double res = underTest.rating(product.getId());
        // then
        assertThat(res).isEqualTo(4.5);
    }

    @Test
    void nbrRatings_returnZero_whenNoRatings() {
        // given
        Product saved = underTest.save(product);
        // when
        int res = underTest.nbrRatings(saved);
        // then
        assertThat(res).isZero();
    }

    @Test
    void nbrRatings_returnNumberOfRatings_whenRatings() {
        // given
        Product saved = underTest.save(product);
        Rating rating1 = generateRating("this is cool", 4);
        Rating rating2 = generateRating("this is great", 5);
        ratingRepository.saveAll(List.of(rating1, rating2));
        // when
        int res = underTest.nbrRatings(saved);
        // then
        assertThat(res).isEqualTo(2);
    }

    @Test
    void countByCategoryId_returnsZero_whenNoProduct() {
        // when
        int res = underTest.countByCategoryId(category.getId());
        // then
        assertThat(res).isZero();
    }
    @Test
    void countByCategoryId_returnsValue_whenProduct() {
        // given
        underTest.save(product);
        // when
        int res = underTest.countByCategoryId(category.getId());
        // then
        assertThat(res).isEqualTo(1);
    }

    @Test
    void existsByName_returnTrue_whenExists() {
        product = underTest.save(product);

        boolean res = underTest.existsByName(name);

        assertThat(res).isTrue();
    }

    @Test
    void existsByName_returnFalse_whenNotExists() {

        boolean res = underTest.existsByName(name);

        assertThat(res).isFalse();
    }

    @Test
    void findByStatusAndCategory() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.findByStatusAndCategory(product.getStatus(), category, PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void findByStatus() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.findByStatus(product.getStatus(), PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void findByCategory() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.findByCategory(category, PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void searchProducts_Returns_whenSearchingByName() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.searchProducts(name, PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();

    }

    @Test
    void searchProducts_Returns_whenSearchingByDescription() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.searchProducts(des, PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void searchProducts_Returns_whenSearchingByCategory() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.searchProducts("cat1", PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void searchProducts_ReturnsEmptyList_whenUnexistingKeyword() {
        // given
        product = underTest.save(product);
        // when
        Page<Product> res = underTest.searchProducts("something", PageRequest.of(0,10));
        // then
        assertThat(res.getContent()).isEmpty();
    }

    private Rating generateRating(String comment, int rating){
        return Rating.builder()
                .rating(rating)
                .comment(comment)
                .product(product)
                .customer(customer)
                .build();
    }
}