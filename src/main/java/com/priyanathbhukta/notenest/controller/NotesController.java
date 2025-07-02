package com.priyanathbhukta.notenest.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.priyanathbhukta.notenest.dto.FavouriteNotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesResponse;
import com.priyanathbhukta.notenest.endpoint.NotesControllerEndpoint;
import com.priyanathbhukta.notenest.entity.FileDetails;
import com.priyanathbhukta.notenest.entity.Notes;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.repository.NotesRepository;
import com.priyanathbhukta.notenest.service.NotesService;
import com.priyanathbhukta.notenest.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController implements NotesControllerEndpoint {
	
	@Autowired
	private NotesService notesService;
	
	private NotesRepository notesRepo;
	
	@Override
	public ResponseEntity<?> saveNotes(@RequestParam String notes,
			@RequestParam (required = false)MultipartFile file) throws Exception{
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if(saveNotes) {
			return CommonUtil.createBuildResponseMessage("notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception{
		
		FileDetails fileDetails = notesService.getFileDetails(id);	
		byte[] data =  notesService.downloadFile(fileDetails);
		
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
		return ResponseEntity.ok().headers(headers).body(data);
		
	}
	
	@Override
	public ResponseEntity<?> getAllNotes(){
		List<NotesDto> notes = notesService.getAllNotes();
		if(CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build()	;
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> seacrhNotes(@RequestParam(name="key", defaultValue = "")String key,@RequestParam(name="pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = "10") Integer pageSize){
		
		NotesResponse notes = notesService.getNotesByUserSearch(pageNo,pageSize,key);
//		if(CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build()	;
//		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name="pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = "10") Integer pageSize){
		
		NotesResponse notes = notesService.getAllNotesByUser(pageNo,pageSize);
//		if(CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build()	;
//		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception{
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Deleted successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception{
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Restored successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception{	
		List<NotesDto> notes = notesService.getUserRecycleBinNotes();
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Nothing in BIN", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception{
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Deleted successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception{	
		notesService.emptyRecycleBin();
		return CommonUtil.createBuildResponseMessage("Deleted successfully", HttpStatus.OK);
	}
	@Override
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception{	
		notesService.favouriteNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Note added to Favourite", HttpStatus.CREATED);
	}
	@Override
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception{	
		notesService.unFavouriteNotes(favNoteId);
		return CommonUtil.createBuildResponseMessage("Notes remove from Favourite", HttpStatus.OK);
	}
	@Override	
	public ResponseEntity<?> getUserFavouriteNote() throws Exception{	
		List<FavouriteNotesDto> userFavouriteNotes = notesService.getUserFavoriteNotes();
		if(CollectionUtils.isEmpty(userFavouriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
		Integer userId = CommonUtil.getLoggedInUser().getId(); // You should get this from authentication context
	    Boolean copyNotes = notesService.copyNotes(id, userId);
	    if (copyNotes) {
	        return CommonUtil.createBuildResponseMessage("Note copied successfully", HttpStatus.CREATED);
	    }
	    return CommonUtil.createErrorResponseMessage("Copy failed! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<byte[]> exportNoteToPdf(@PathVariable Integer id) throws Exception {
	    try {
	        byte[] data = notesService.exportNoteAsPdf(id);
	        
	        // Get proper filename from service
	        String filename;
	        try {
	            filename = notesService.getNoteFilename(id, "pdf");
	        } catch (Exception e) {
	            filename = "note_" + id + ".pdf";
	        }
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
	        return new ResponseEntity<>(data, headers, HttpStatus.OK);
	        
	    } catch (ResourceNotFoundException e) {
	        return (ResponseEntity<byte[]>) CommonUtil.createErrorResponseMessage("Note not found", HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        return (ResponseEntity<byte[]>) CommonUtil.createErrorResponseMessage("Error exporting note: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<byte[]> exportNoteToDocx(@PathVariable Integer id) throws Exception {
	    try {
	        byte[] data = notesService.exportNoteAsDocx(id);
	        
	        // Get proper filename from service
	        String filename;
	        try {
	            filename = notesService.getNoteFilename(id, "docx");
	        } catch (Exception e) {
	            filename = "note_" + id + ".docx";
	        }
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
	        return new ResponseEntity<>(data, headers, HttpStatus.OK);
	        
	    } catch (ResourceNotFoundException e) {
	        return (ResponseEntity<byte[]>) CommonUtil.createErrorResponseMessage("Note not found", HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        return (ResponseEntity<byte[]>) CommonUtil.createErrorResponseMessage("Error exporting note: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private String sanitizeFilename(String title) {
		Object filename = null;
		if (filename == null || ((String) filename).trim().isEmpty()) {
	        return "note";
	    }
	    // Remove invalid characters and limit length
	    String sanitized = ((String) filename).replaceAll("[^a-zA-Z0-9._-]", "_");
	    return sanitized.length() > 50 ? sanitized.substring(0, 50) : sanitized;
	}
	
	@Override
	public ResponseEntity<byte[]> exportAllNotesToPdf() throws Exception {
	    try {
	    	Integer userId = CommonUtil.getLoggedInUser().getId();	 // You should get this from authentication context
	        byte[] data = notesService.exportAllNotesAsPdf(userId);
	        
	        String filename = "all_notes_" + LocalDate.now() + ".pdf";
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
	        return new ResponseEntity<>(data, headers, HttpStatus.OK);
	        
	    } catch (Exception e) {
	        return (ResponseEntity<byte[]>) CommonUtil.createErrorResponseMessage("Error exporting all notes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
