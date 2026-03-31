package org.example.ecommerceapi.service;

import org.example.ecommerceapi.dto.product.CreateProductDTO;
import org.example.ecommerceapi.dto.product.ProductResponseDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.dto.product.UpdateProductDTO;
import org.example.ecommerceapi.entity.Category;
import org.example.ecommerceapi.entity.Product;
import org.example.ecommerceapi.enums.ProductStatus;
import org.example.ecommerceapi.exception.BadRequestException;
import org.example.ecommerceapi.exception.ResourceNotFoundException;
import org.example.ecommerceapi.repository.CategoryRepository;
import org.example.ecommerceapi.repository.ProductRepository;
import org.example.ecommerceapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author $(bilal belhaj)
 **/
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private ProductServiceImpl underTest;
    private Category category;
    private Product product;
    private CreateProductDTO dto;
    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("cat")
                .description("des")
                .build();

        product = Product.builder()
                .name("prod")
                .description("desc")
                .price(BigDecimal.TEN)
                .stock(12)
                .status(ProductStatus.ACTIVE)
                .category(category)
                .imageType("png")
                .imageData(new byte[12])
                .build();

        dto = new CreateProductDTO(
                "prod",
                "desc",
                BigDecimal.TEN,
                12,
                1L
        );
    }

    @Test
    void canGetAllProducts(){
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductSummaryDTO> res = underTest.getAllProducts();

        assertThat(res).isNotEmpty();
    }

    @Test
    void canGetProducts_withStatusAndCategory(){
        category.setId(1L);
        product.setStatus(ProductStatus.EXPIRED);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByStatusAndCategory(eq(ProductStatus.EXPIRED), eq(category),any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductSummaryDTO> res = underTest.getProducts(ProductStatus.EXPIRED, 1L, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void canGetProducts_withStatusOnly(){
        product.setStatus(ProductStatus.EXPIRED);
        when(productRepository.findByStatus(eq(ProductStatus.EXPIRED),any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductSummaryDTO> res = underTest.getProducts(ProductStatus.EXPIRED, null, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void canGetProducts_withCategoryOnly(){
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(eq(category),any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductSummaryDTO> res = underTest.getProducts(null, 1L, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void canGetProducts_withNoFilters(){
        when(productRepository.findByStatus(eq(ProductStatus.ACTIVE),any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductSummaryDTO> res = underTest.getProducts(null, null, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    // search //
    @ParameterizedTest
    @ValueSource(strings = {"desc", "prod", "pro", "cat"})
    void canSearchProducts_returnsResults(String q){
        when(productRepository.searchProducts(eq(q), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductSummaryDTO> res = underTest.search(q, 0, 10);

        assertThat(res.getContent()).isNotEmpty();
    }

    @Test
    void canGetProductById_whenExists(){
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO res = underTest.getProduct(1L);

        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    void getProductById_throws_whenNotExists(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getProduct(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canAddProduct_whenValid(){
        category.setId(1L);
        when(productRepository.existsByName(dto.name())).thenReturn(false);
        when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );

        MultipartFile image = new MockMultipartFile(
                "image",                 // form field name
                "test-image.png",        // original filename
                "image/png",             // content type
                "fake-image-content".getBytes() // file content
        );

        ProductResponseDTO res = underTest.addProduct(dto, image);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productArgumentCaptor.capture());
        Product saved = productArgumentCaptor.getValue();
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(saved.getId());
    }

    @Test
    void addProduct_throws_whenNameAlreadyExists(){
        when(productRepository.existsByName(dto.name())).thenReturn(true);

        assertThatThrownBy(() -> underTest.addProduct(dto, null)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void addProduct_throws_whenCategoryNotFound(){
        when(productRepository.existsByName(dto.name())).thenReturn(false);
        when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addProduct(dto, null)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addProduct_throws_whenImageFileIsNull(){
        when(productRepository.existsByName(dto.name())).thenReturn(false);
        when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> underTest.addProduct(dto, null)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void canUpdateProduct_whenExists(){
        String updatedName = "prodUpdate";
        UpdateProductDTO updateDto = new UpdateProductDTO(updatedName, "desc", null, null, null);
        product.setId(1L);
        category.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(
                inv -> inv.getArgument(0)
        );
        MultipartFile image = new MockMultipartFile(
                "name",
                "image",
                "image/png",
                "fakedata".getBytes()
        );
        ProductResponseDTO res = underTest.updateProduct(1L, updateDto, image);
        assertThat(res).isNotNull();
        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.name()).isEqualTo(updatedName);

    }

    @Test
    void updateProduct_throws_whenProductNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateProductDTO updateDto = new UpdateProductDTO("name", "desc", null, null, null);
        assertThatThrownBy(() -> underTest.updateProduct(1L, updateDto, null)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void canDeleteProduct_whenExists(){
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        underTest.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_throws_whenProductNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteProduct(1L)).isInstanceOf(ResourceNotFoundException.class);
    }
}