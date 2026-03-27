package org.example.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@Tag(name = "categories", description = "operations related to categories")
@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "add category",
            description = "endpoint to add new category"
    )
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        CategoryResponseDTO res = categoryService.addCategory(createCategoryDTO);
        URI uri = URI.create("http://localhost:8081" + res.id());
        return ResponseEntity.created(uri).body(res);
    }

    // read
    @GetMapping
    @Operation(
            summary = "get categories",
            description = "public endpoint to get all categories"
    )
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        return ResponseEntity.ok().body(
                categoryService.getCategories()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "{categoryId}")
    @Operation(
            summary = "get category details",
            description = "returns more details about a category"
    )
    public ResponseEntity<CategoryResponseDTO> getCategory(@Parameter(description = "Category Id") @PathVariable("categoryId") Long id) {
        return ResponseEntity.ok().body(
                categoryService.getCategory(id)
        );
    }

    // update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "{categoryId}")
    @Operation(
            summary = "update category",
            description = "update a category "
    )
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "Category Id") @PathVariable("categoryId") Long id,
            @Parameter(description = "new category data") @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO
            ) {
        return ResponseEntity.ok().body(
                categoryService.updateCategory(id, updateCategoryDTO)
        );
    }
    // delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{categoryId}")
    @Operation(
            summary = "Delete category",
            description = "delete a category"
    )
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "Category Id") @PathVariable("categoryId") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // get products
    @GetMapping("{id}/products")
    @Operation(
            summary = "get category's products",
            description = "return all products belongs to a category"
    )
    public ResponseEntity<List<ProductSummaryDTO>> getProducts(@Parameter(description = "Category Id") @PathVariable Long id) {
        return ResponseEntity.ok().body(categoryService.getProductsByCategory(id));
    }
}
