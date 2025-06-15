package com.priyanathbhukta.notenest.service;

import java.util.List;

import com.priyanathbhukta.notenest.entity.Category;

public interface CategoryService {
	
	public Boolean saveCategory(Category category);
	
	public List<Category> getAllCategory();

}
