package com.priyanathbhukta.notenest.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findByIsActiveTrue();

	
	
	
}
