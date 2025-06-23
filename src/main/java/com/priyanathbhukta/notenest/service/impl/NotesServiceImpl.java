package com.priyanathbhukta.notenest.service.impl;

import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto.CategoryDto;
import com.priyanathbhukta.notenest.entity.Notes;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.repository.CategoryRepository;
import com.priyanathbhukta.notenest.repository.NotesRepository;
import com.priyanathbhukta.notenest.service.NotesService;


@Service
public class NotesServiceImpl implements  NotesService{
	
	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	
//	@Override
//	public Boolean saveNotes(NotesDto notesDto) throws Exception {
//		
//		//category validation
//		checkCategoryExist(notesDto.getCategory());
//		
//		Notes notes = mapper.map(notesDto, Notes.class);
//		
//		Notes saveNotes = notesRepo.save(notes);
//		
//		return saveNotes != null && saveNotes.getId() != null;
//
//	}
	
	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {

	    // category validation
	    checkCategoryExist(notesDto.getCategory());

	    Notes notes = mapper.map(notesDto, Notes.class);
	    Notes saveNotes = notesRepo.save(notes);

	    if (!ObjectUtils.isEmpty(saveNotes)) {
	        return true;
	    }

	    return false;
	}


	private void checkCategoryExist(CategoryDto category) throws Exception {
		// TODO Auto-generated method stub
		
		categoryRepo.findById(category.getId())
			.orElseThrow(()->new ResourceNotFoundException("Category id invalid"));
		
	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepo
				.findAll()
				.stream()
				.map(note->mapper.map(note, NotesDto.class))
				.toList();
		 
	}

}
