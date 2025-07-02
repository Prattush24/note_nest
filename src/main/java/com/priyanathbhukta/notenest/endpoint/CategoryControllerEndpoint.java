package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.exception.ExistDataException;

public interface CategoryControllerEndpoint {
	
	
	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws ExistDataException;
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllCategory();
	
	@GetMapping("/active")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getActiveCategory();
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
	
	
}
