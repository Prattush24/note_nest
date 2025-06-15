package com.priyanathbhukta.notenest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.CategoryResponse;
import com.priyanathbhukta.notenest.entity.Category;
import com.priyanathbhukta.notenest.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	
//	save-category api
	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
		
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if(saveCategory) {	
			return new ResponseEntity<> ("saved success",HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<> ("not saved",HttpStatus.INTERNAL_SERVER_ERROR); 
		}	
	}
	
	
//	Category api for get all categories
	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory(){
		
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();	
		}else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/active-category")
	public ResponseEntity<?> getActiveCategory(){
		
		List<CategoryResponse> activeCategory = categoryService.getActiveCategory();
		
		if(CollectionUtils.isEmpty(activeCategory)) {
			return ResponseEntity.noContent().build();	
		}else {
			return new ResponseEntity<>(activeCategory, HttpStatus.OK);
		}
		
	}
	
	
}
