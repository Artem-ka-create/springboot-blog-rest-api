package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(long categoryId);

    List<CategoryDto> getAllCategories();

    CategoryDto updateCategory(CategoryDto categoryDto, long id);

    void deleteCategory(long id);

}
