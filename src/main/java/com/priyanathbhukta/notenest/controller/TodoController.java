package com.priyanathbhukta.notenest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyanathbhukta.notenest.dto.TodoDto;
import com.priyanathbhukta.notenest.endpoint.TodoControllerEndpoint;
import com.priyanathbhukta.notenest.service.TodoService;
import com.priyanathbhukta.notenest.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController implements TodoControllerEndpoint{
	
	@Autowired
	private TodoService todoService;
	
	
	@Override
	public ResponseEntity<?> saveTodo( TodoDto todo) throws Exception{
		Boolean saveTodo = todoService.saveTodo(todo);
		if(saveTodo) {
			return CommonUtil.createBuildResponseMessage("Todo saved", HttpStatus.CREATED);
		}else {
			return CommonUtil.createErrorResponseMessage("Todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public ResponseEntity<?> getTodoById( Integer id) throws Exception {
	    TodoDto todo = todoService.getTodoById(id);
	    return CommonUtil.createBuildResponse(todo, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getAllTodoByUser() throws Exception {
	    List<TodoDto> todoList = todoService.getTodoByUser();
	    if(CollectionUtils.isEmpty(todoList)) {
	    	return ResponseEntity.noContent().build();
	    }
	    return CommonUtil.createBuildResponse(todoList, HttpStatus.OK);
	}
	
}
