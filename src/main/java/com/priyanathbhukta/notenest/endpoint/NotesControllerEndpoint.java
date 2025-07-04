package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.priyanathbhukta.notenest.util.Constants.ROLE_USER;
import static com.priyanathbhukta.notenest.util.Constants.ROLE_ADMIN;
import static com.priyanathbhukta.notenest.util.Constants.DEFAULT_PAGE_NO;
import static com.priyanathbhukta.notenest.util.Constants.DEFAULT_PAGE_SIZE;


@Tag(name = "User Notes",description = "Provides operations for creating, searching, downloading, exporting, favoriting, and managing notes, including recycle bin and file attachments.")
@RequestMapping("/api/v1/notes")
public interface NotesControllerEndpoint {
	
	@Operation(summary = "Create and save a new note",
			description = "Allows users to save a new note with optional file attachment. The note content and file are stored securely for future retrieval.",
			tags = {"User Notes"})
	@PostMapping(value = "/", consumes = "multipart/form-data")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(@RequestParam 
			@Parameter(description = "JSON String Notes", required = true,
			content = @Content(schema = @Schema(implementation = NotesRequest.class)))
			String notes,
			@RequestParam (required = false)MultipartFile file) throws Exception;
	
	@Operation(summary = "Download attached file by note ID",
			description = "Allows admin users to download the file associated with a specific note by providing the note ID.",
			tags = {"User Notes"})
	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get all notes (admin only)",
			description = "Retrieves a complete list of all notes in the system. This endpoint is only accessible by admin users.",
			tags = {"User Notes"})
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@Operation(summary = "Search notes by keyword",
			description = "Allows users to search their notes using a keyword with support for pagination. Returns results based on title or content match.",
			tags = {"User Notes"})
	@GetMapping("/search")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> seacrhNotes(@RequestParam(name="key", defaultValue = "")String key,@RequestParam(name="pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@Operation(summary = "Get all notes of the logged-in user",
			description = "Returns a paginated list of all notes created by the currently authenticated user.",
			tags = {"User Notes"})
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name="pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
	@RequestParam(name="pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@Operation(summary = "Soft delete a note",
			description = "Soft deletes a specific note by moving it to the recycle bin instead of permanent deletion.",
			tags = {"User Notes"})
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Restore note from recycle bin",
			description = "Restores a previously deleted note from the user's recycle bin.",
			tags = {"User Notes"})
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get notes in recycle bin",
			description = "Retrieves all notes currently placed in the recycle bin by the logged-in user.",
			tags = {"User Notes"})
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@Operation(summary = "Permanently delete a note",
			description = "Permanently deletes a note from the recycle bin using the note ID. This action cannot be undone.",
			tags = {"User Notes"})
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Empty the entire recycle bin",
			description = "Permanently deletes all notes from the user's recycle bin.",
			tags = {"User Notes"})
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@Operation(summary = "Mark a note as favorite",
			description = "Marks a note as a favorite for the logged-in user using the note ID.",
			tags = {"User Notes"})
	@GetMapping("/fav/{noteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;
	
	@Operation(summary = "Remove note from favorites",
			description = "Removes a note from the user's list of favorite notes using the favorite note ID.",
			tags = {"User Notes"})
	@DeleteMapping("/un-fav/{favNoteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception;
	
	@Operation(summary = "Get all favorite notes",
			description = "Retrieves all notes marked as favorite by the logged-in user.",
			tags = {"User Notes"})
	@GetMapping("/fav-notes")
	@PreAuthorize(ROLE_USER)	
	public ResponseEntity<?> getUserFavouriteNote() throws Exception;
	
	@Operation(summary = "Create a copy of a note",
			description = "Duplicates an existing note for the logged-in user based on the given note ID.",
			tags = {"User Notes"})
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Export a note to PDF",
			description = "Exports a single note to a downloadable PDF file for the logged-in user.",
			tags = {"User Notes"})
	@GetMapping("/export/{id}/pdf")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportNoteToPdf(@PathVariable Integer id) throws Exception ;
	
	@Operation(summary = "Export a note to DOCX",
			description = "Exports a single note to a downloadable DOCX file for the logged-in user.",
			tags = {"User Notes"})
	@GetMapping("/export/{id}/docx")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportNoteToDocx(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Export all notes to a single PDF file",
			description = "Exports all of the user's notes into one PDF document for downloading.",
			tags = {"User Notes"})
	@GetMapping("/export/all/pdf")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<byte[]> exportAllNotesToPdf() throws Exception;
	
	
}
