package org.example.ecommerceapi.controller;

import org.example.ecommerceapi.dto.category.CategoryResponseDTO;
import org.example.ecommerceapi.dto.category.CreateCategoryDTO;
import org.example.ecommerceapi.dto.category.UpdateCategoryDTO;
import org.example.ecommerceapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $(bilal belhaj)
 **/

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // create
    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        return categoryService.addCategory(createCategoryDTO);
    }
    // read
    @GetMapping
    public List<CategoryResponseDTO> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping(path = "{categoryId}")
    public CategoryResponseDTO getCategory(@PathVariable("categoryId") Long id) {
        return categoryService.getCategory(id);
    }

    // update
    @PutMapping(path = "{categoryId}")
    public CategoryResponseDTO updateCategory(
            @PathVariable("categoryId") Long id,
            @RequestBody UpdateCategoryDTO updateCategoryDTO
            ) {
        return categoryService.updateCategory(id, updateCategoryDTO);
    }
    // delete
    @DeleteMapping(path = "{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Long id) {
        categoryService.deleteCategory(id);
    }
}
