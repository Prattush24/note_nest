package com.priyanathbhukta.notenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);

	boolean existsByMobNo(String mobNo);

	User findByEmail(String username);
	
}
