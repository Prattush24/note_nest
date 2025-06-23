package com.priyanathbhukta.notenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Integer>{

}
