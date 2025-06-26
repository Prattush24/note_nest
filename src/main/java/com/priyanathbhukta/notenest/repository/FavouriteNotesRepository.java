package com.priyanathbhukta.notenest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer>{

	List<FavouriteNotes> findByUserId(int userId);

}
