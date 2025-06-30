package com.priyanathbhukta.notenest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.CategoryResponse;
import com.priyanathbhukta.notenest.entity.Category;
import com.priyanathbhukta.notenest.exception.ExistDataException;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.exception.ValidationException;
import com.priyanathbhukta.notenest.service.CategoryService;
import com.priyanathbhukta.notenest.util.CommonUtil;
import com.priyanathbhukta.notenest.util.Validation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private Validation validation;
	
//	save-category api
	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws ExistDataException{
		
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if(saveCategory) {	
			return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
		}
		else {
			return CommonUtil.createBuildResponseMessage("failed to save", HttpStatus.INTERNAL_SERVER_ERROR); 
		}	
	}
	
	
//	Category api for get all categories
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllCategory(){
//		String nm =null;
//		nm.toUpperCase();
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();	
		}else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/active")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getActiveCategory(){
		
		List<CategoryResponse> activeCategory = categoryService.getActiveCategory();
		
		if(CollectionUtils.isEmpty(activeCategory)) {
			return ResponseEntity.noContent().build();	
		}else {
//			return new ResponseEntity<>(activeCategory, HttpStatus.OK);
			return CommonUtil.createBuildResponse(activeCategory, HttpStatus.OK);
		}
		
	}
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception{
		

		CategoryDto categoryDto = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryDto)){
//			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
			return CommonUtil.createErrorResponseMessage("Category not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
//			return new ResponseEntity<>(categoryDto, HttpStatus.OK);
			return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
		
		Boolean deleted = categoryService.deleteCtegory(id);
		if(deleted) {
			return CommonUtil.createBuildResponse("Category deleted successfully", HttpStatus.OK);
//			return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
		}
		else {
			return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
//			return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
