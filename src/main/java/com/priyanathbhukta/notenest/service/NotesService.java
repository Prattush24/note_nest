package com.priyanathbhukta.notenest.service;

import java.util.List;

import com.priyanathbhukta.notenest.dto.NotesDto;

public interface NotesService {
	public Boolean saveNotes(NotesDto notesDto) throws Exception;
	
	public List<NotesDto> getAllNotes();
}
