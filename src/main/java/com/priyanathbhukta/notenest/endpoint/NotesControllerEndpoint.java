package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import static com.priyanathbhukta.notenest.util.Constants.ROLE_USER;
import static com.priyanathbhukta.notenest.util.Constants.ROLE_ADMIN;
import static com.priyanathbhukta.notenest.util.Constants.DEFAULT_PAGE_NO;
import static com.priyanathbhukta.notenest.util.Constants.DEFAULT_PAGE_SIZE;




public interface NotesControllerEndpoint {
	
	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(@RequestParam String notes,
			@RequestParam (required = false)MultipartFile file) throws Exception;
	
	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/search")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> seacrhNotes(@RequestParam(name="key", defaultValue = "")String key,@RequestParam(name="pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name="pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;
	
	@DeleteMapping("/un-fav/{favNoteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception;
	
	@GetMapping("/fav-notes")
	@PreAuthorize(ROLE_USER)	
	public ResponseEntity<?> getUserFavouriteNote() throws Exception;
	
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/export/{id}/pdf")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportNoteToPdf(@PathVariable Integer id) throws Exception ;
	
	@GetMapping("/export/{id}/docx")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportNoteToDocx(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/export/all/pdf")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportAllNotesToPdf() throws Exception;
	
	
}
