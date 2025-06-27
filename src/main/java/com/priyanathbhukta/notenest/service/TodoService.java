package com.priyanathbhukta.notenest.service;

import java.util.List;

import com.priyanathbhukta.notenest.dto.TodoDto;

public interface TodoService {
	
	public Boolean saveTodo(TodoDto todoDto) throws Exception;
	
	public TodoDto getTodoById(Integer id) throws Exception;
	
	public List<TodoDto> getTodoByUser();

	
}
