package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface NotesControllerEndpoint {
	
	@PostMapping("/")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes,
			@RequestParam (required = false)MultipartFile file) throws Exception;
	
	@GetMapping("/download/{id}")
	@PreAuthorize("hasaNYRole('USER','ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/search")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> seacrhNotes(@RequestParam(name="key", defaultValue = "")String key,@RequestParam(name="pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = "10") Integer pageSize);
	
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name="pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = "10") Integer pageSize);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;
	
	@DeleteMapping("/un-fav/{favNoteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception;
	
	@GetMapping("/fav-notes")
	@PreAuthorize("hasRole('USER')")	
	public ResponseEntity<?> getUserFavouriteNote() throws Exception;
	
	@GetMapping("/copy/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/export/{id}/pdf")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<byte[]> exportNoteToPdf(@PathVariable Integer id) throws Exception ;
	
	@GetMapping("/export/{id}/docx")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<byte[]> exportNoteToDocx(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/export/all/pdf")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<byte[]> exportAllNotesToPdf() throws Exception;
	
	
}
