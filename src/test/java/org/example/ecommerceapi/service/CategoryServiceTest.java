package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.impl.CategoryServiceImpl;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private CategoryServiceImpl underTest;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("cat")
                .description("desc")
                .build();
    }

    @Test
    void canGetAllCategories(){
        underTest.getCategories();
        verify(categoryRepository).findAll();
    }

    @Test
    void canGetCategoryById_whenExists(){
        // given
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        // when
        CategoryResponseDTO res = underTest.getCategory(1L);
        // then
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    void getCategoryById_throws_whenNotExists(){
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.getCategory(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canCreateCategory_whenNameNotExists(){
        // given
        when(categoryRepository.existsByName("cat")).thenReturn(false);

        when(categoryRepository.save(any(Category.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        // when
        CategoryResponseDTO res = underTest.addCategory(new CreateCategoryDTO("cat", "des"));

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        // then
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category saved = categoryArgumentCaptor.getValue();
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(saved.getId());

    }

    @Test
    void createCategory_throws_whenNameAlreadyExists(){
        // given
        when(categoryRepository.existsByName("cat")).thenReturn(true);
        CreateCategoryDTO dto = new CreateCategoryDTO("cat", "des");
        // then
        assertThatThrownBy(() -> underTest.addCategory(dto)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void canUpdateCategory_whenExistsAndNameNotTaken(){
        // given
        String newName = "catUpdate";
        UpdateCategoryDTO dto = new UpdateCategoryDTO(newName, null);
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        // when
        CategoryResponseDTO res = underTest.updateCategory(1L, dto);
        // then
        assertThat(res).isNotNull();
        assertThat(res.name()).isEqualTo(newName);
        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    void updateCategory_throws_whenCategoryNotExists(){
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateCategoryDTO dto = new UpdateCategoryDTO("cat1", null);
        // then
        assertThatThrownBy(() -> underTest.updateCategory(1L, dto)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateCategory_throws_whenNameAlreadyExists(){
        // given
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("cat1")).thenReturn(true);
        UpdateCategoryDTO dto = new UpdateCategoryDTO("cat1", null);
        // then
        assertThatThrownBy(() -> underTest.updateCategory(1L, dto)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void canDeleteCategory_whenExists(){
        // given
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        // when
        underTest.deleteCategory(1L);
        // then
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_throws_whenNotExists(){
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.deleteCategory(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getProductsByCategory_returns_whenProductsExists() {
        // given
        category.setId(1L);
        Product product = Product.builder()
                .name("product")
                .description("description")
                .imageData(new byte[12])
                .imageType("png")
                .stock(12)
                .id(2L)
                .price(BigDecimal.TEN)
                .category(category)
                .status(ProductStatus.ACTIVE)
                .build();
        category.getProducts().add(product);
        when(productRepository.nbrRatings(product)).thenReturn(2);
        when(productRepository.rating(2L)).thenReturn(4.5);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        // when
        List<ProductSummaryDTO> res = underTest.getProductsByCategory(1L);
        // then
        assertThat(res).isNotEmpty();
    }
    @Test
    void getProductsByCategory_throws_whenCategoryNotExists() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.getProductsByCategory(1L)).isInstanceOf(ResourceNotFoundException.class);
    }
}