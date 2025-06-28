package com.priyanathbhukta.notenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
}
