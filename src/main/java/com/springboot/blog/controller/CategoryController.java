package com.springboot.blog.controller;

import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.addCategory(categoryDto),HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable("id") long countryId){
        return new ResponseEntity<>(categoryService.getCategory(countryId),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllCategories(){
        return new ResponseEntity<>( categoryService.getAllCategories(),HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable("id") long categoryId){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category successfuly deleted", HttpStatus.OK);
    }
}
