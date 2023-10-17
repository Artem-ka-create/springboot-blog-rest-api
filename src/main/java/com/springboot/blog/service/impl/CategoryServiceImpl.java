package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(ModelMapper mapper, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category category = mapToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return mapToDto(savedCategory);
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        return mapToDto(categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", categoryId)));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> cateories = categoryRepository.findAll();

        return cateories.stream().map((category) -> mapToDto(category)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", id));

        foundCategory.setName(categoryDto.getName());
        foundCategory.setDescription(categoryDto.getDescription());
        foundCategory.setId(id);

        return mapToDto(categoryRepository.save(foundCategory));
    }

    @Override
    public void deleteCategory(long id) {

        categoryRepository.delete(categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", id)));
    }


    private CategoryDto mapToDto(Category category){
        CategoryDto categoryDto = mapper.map(category,CategoryDto.class);
        return categoryDto;
    }
    private Category mapToEntity(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        return category;
    }
}
