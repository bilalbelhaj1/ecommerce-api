package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $(bilal belhaj)
 **/
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository underTest;
    private Category category;
    private final String name = "categoryName";

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name(name)
                .description("basic description ")
                .build();
    }

    @Test
    void existsByName_shouldReturnTrue_whenCategoryExists() {
        // given
        underTest.save(category);
        // when
        boolean res = underTest.existsByName(name);
        // then
        assertThat(res).isTrue();
    }

    @Test
    void existsByName_shouldReturnFalse_whenCategoryNotExists() {
        boolean res = underTest.existsByName(name);
        assertThat(res).isFalse();
    }
}