package org.example.ecommerceapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.dto.product.ProductSummaryDTO;
import org.example.ecommerceapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // create
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        CategoryResponseDTO res = categoryService.addCategory(createCategoryDTO);
        URI uri = URI.create("http://localhost:8081" + res.id());
        return ResponseEntity.created(uri).body(res);
    }
    // read
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        return ResponseEntity.ok().body(
                categoryService.getCategories()
        );
    }

    @GetMapping(path = "{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable("categoryId") Long id) {
        return ResponseEntity.ok().body(
                categoryService.getCategory(id)
        );
    }

    // update
    @PutMapping(path = "{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable("categoryId") Long id,
            @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO
            ) {
        return ResponseEntity.ok().body(
                categoryService.updateCategory(id, updateCategoryDTO)
        );
    }
    // delete
    @DeleteMapping(path = "{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // get products
    @GetMapping("{id}/products")
    public ResponseEntity<List<ProductSummaryDTO>> getProducts(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryService.getProductsByCategory(id));
    }
}
