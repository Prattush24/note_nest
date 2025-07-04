package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.priyanathbhukta.notenest.dto.TodoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static com.priyanathbhukta.notenest.util.Constants.ROLE_USER;



@Tag(name = "TODO",description = "Provides operations to create, retrieve by ID, and list all TODO tasks for the authenticated user.")
@RequestMapping("/api/v1/todo")
public interface TodoControllerEndpoint {
	
	@Operation(summary = "Create a new TODO task",
			description = "Creates a new TODO task for the authenticated user based on the provided task details.",
			tags = {"TODO"})
	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todo) throws Exception;
	
	
	@Operation(summary = "Retrieve a TODO task by ID",
			description = "Fetches a specific TODO task by its unique ID for the authenticated user.",
			tags = {"TODO"})
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception ;
	
	
	@Operation(summary = "List all TODO tasks",
			description = "Retrieves a list of all TODO tasks associated with the authenticated user.",
			tags = {"TODO"})
	@GetMapping("/list")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllTodoByUser() throws Exception ;
	
}
