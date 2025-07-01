package com.priyanathbhukta.notenest.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.priyanathbhukta.notenest.entity.Notes;
import com.priyanathbhukta.notenest.repository.NotesRepository;

@Component
public class NotesSchedular {
	
	@Autowired
	private NotesRepository notesRepo;

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotesSchedular() {
		
		LocalDateTime cuttOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deleteNotes =  notesRepo.findAllByIsDeletedAndDeletedOnBefore(true, cuttOffDate);
		notesRepo.deleteAll(deleteNotes);
	}
	
	
}
