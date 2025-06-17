package com.priyanathbhukta.notenest.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;import org.hibernate.dialect.function.DateTruncEmulation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.priyanathbhukta.notenest.config.ProjectConfig;
import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.CategoryResponse;
import com.priyanathbhukta.notenest.entity.Category;
import com.priyanathbhukta.notenest.repository.CategoryRepository;
import com.priyanathbhukta.notenest.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ProjectConfig projectConfig;
    @Autowired
    private CategoryRepository categoryRepo;
    
    @Autowired
    private ModelMapper mapper;

    CategoryServiceImpl(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
//        Category category = new Category();
//        category.setName(categoryDto.getName());
//        category.setDescription(categoryDto.getDescription());
//        category.setIsActive(categoryDto.getIsActive());
    	
    	Category category = mapper.map(categoryDto, Category.class);
    	if(ObjectUtils.isEmpty(category.getId())) {
    		category.setIsDeleted(false);
            category.setCreatedBy(1);
            category.setCreatedOn(new Date());
    	}else {
    		updateCategory(category);
    	}
    	
        
        Category savedCategory = categoryRepo.save(category);
        return !ObjectUtils.isEmpty(savedCategory);
    }

    private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepo.findById(category.getId());
		if(findById.isPresent()) {
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			
			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}
	}

	@Override
    public List<CategoryDto> getAllCategory() {
    	List<Category> categories = categoryRepo.findByIsDeletedFalse();
    	
    	List<CategoryDto> categoryDtoList = categories.stream().map(cat->mapper.map(cat, CategoryDto.class)).toList();    	
        return 	categoryDtoList;
    }

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> Categorylist = categories.stream().map(cat->mapper.map(cat, CategoryResponse.class)).toList();
		return Categorylist;	
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {
		Optional<Category> findByCategory = categoryRepo.findByIdAndIsDeletedFalse(id);
		if(findByCategory.isPresent()) {
			Category category = findByCategory.get();
			return mapper.map(category, CategoryDto.class);
		}
		return null;
	}

	@Override
	public Boolean deleteCtegory(Integer id) {
		Optional<Category> findByCategory = categoryRepo.findById(id);
		if(findByCategory.isPresent()) {
			Category category = findByCategory.get();
			category.setIsDeleted(true);
			//category.setIsActive(false);
			categoryRepo.save(category);
			return true;
		}
		return false;
	}
	
	
	
     
}