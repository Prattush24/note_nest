package com.priyanathbhukta.notenest.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.priyanathbhukta.notenest.dto.FavouriteNotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesResponse;
import com.priyanathbhukta.notenest.entity.FileDetails;


public interface NotesService {
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	
	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDtls) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id)throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin(int userId)throws Exception;

	public void favouriteNotes(Integer noteId) throws Exception;
	
	public void unFavouriteNotes(Integer noteId) throws Exception;
	

	public List<FavouriteNotesDto> getUserFavoriteNotes() throws Exception;

	public Boolean copyNotes(Integer id, Integer userId) throws Exception;
	
	public byte[] exportNoteAsPdf(Integer noteId) throws Exception;
	
	public byte[] exportNoteAsDocx(Integer noteId) throws Exception;
	
	public String getNoteFilename(Integer noteId, String extension) throws Exception;

	public byte[] exportAllNotesAsPdf(Integer userId) throws Exception;


}
