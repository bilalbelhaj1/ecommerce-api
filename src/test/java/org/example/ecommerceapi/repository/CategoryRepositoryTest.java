package org.example.ecommerceapi.repository;

import org.example.ecommerceapi.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// FIX: Note the corrected package path here
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat; // Standard AssertJ import

@DataJpaTest
// This ensures it uses the H2 database instead of trying to connect to your real Postgres
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository underTest;

    private Category category;
    private final String name = "categoryName";

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name(name)
                .description("basic description")
                .build();
    }

    @Test
    void existsByName_shouldReturnTrue_whenCategoryExists() {
        underTest.save(category);
        boolean res = underTest.existsByName(name);
        assertThat(res).isTrue();
    }

    @Test
    void existsByName_shouldReturnFalse_whenCategoryNotExists() {
        boolean res = underTest.existsByName(name);
        assertThat(res).isFalse();
    }
}