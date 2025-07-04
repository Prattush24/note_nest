package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.exception.ExistDataException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.priyanathbhukta.notenest.util.Constants.ROLE_ADMIN;
import static com.priyanathbhukta.notenest.util.Constants.ROLE_ADMIN_USER;


@Tag(name = "Category",description = "Handles category management operations such as creating, retrieving (all or active), viewing by ID, and deleting categories.")
@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoint {
	
	
	@Operation(summary = "Create a new category",
			description = "Creates and saves a new category using the provided category data. Only accessible to admin users.",
			tags = {"Category"})
	@PostMapping("/save")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws ExistDataException;
	
	@Operation(summary = "Retrieve all categories",
			description = "Returns a list of all categories available in the system. Only accessible to admin users.",
			tags = {"Category"})
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllCategory();
	
	@Operation(summary = "Retrieve all active categories",
			description = "Returns a list of categories that are marked as active. Accessible to both admin and user roles.",
			tags = {"Category"})
	@GetMapping("/active")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> getActiveCategory();
	
	@Operation(summary = "Get category details by ID",
			description = "Retrieves detailed information of a category based on the provided category ID. Only accessible to admin users.",
			tags = {"Category"})
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Delete a category by ID",
			description = "Deletes a specific category from the system using its unique ID. Only accessible to admin users.",
			tags = {"Category"})
	@DeleteMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
	
}