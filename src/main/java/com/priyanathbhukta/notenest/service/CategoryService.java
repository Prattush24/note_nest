package com.priyanathbhukta.notenest.service;

import java.util.List;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.CategoryResponse;
import com.priyanathbhukta.notenest.entity.Category;

public interface CategoryService {
	
	public Boolean saveCategory(CategoryDto categoryDto);
	
	public List<CategoryDto> getAllCategory();

	public List<CategoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id) throws Exception;

	public Boolean deleteCtegory(Integer id);

}
		