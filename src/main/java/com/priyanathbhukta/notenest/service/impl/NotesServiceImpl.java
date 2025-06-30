package com.priyanathbhukta.notenest.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.catalina.mapper.Mapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.priyanathbhukta.notenest.dto.FavouriteNotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto.CategoryDto;
import com.priyanathbhukta.notenest.dto.NotesDto.FilesDto;
import com.priyanathbhukta.notenest.dto.NotesResponse;
import com.priyanathbhukta.notenest.entity.FavouriteNotes;
import com.priyanathbhukta.notenest.entity.FileDetails;
import com.priyanathbhukta.notenest.entity.Notes;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.exception.UnauthorizedException;
import com.priyanathbhukta.notenest.repository.CategoryRepository;
import com.priyanathbhukta.notenest.repository.FavouriteNotesRepository;
import com.priyanathbhukta.notenest.repository.FileRepository;
import com.priyanathbhukta.notenest.repository.NotesRepository;
import com.priyanathbhukta.notenest.service.NotesService;
import com.priyanathbhukta.notenest.util.CommonUtil;


@Service
public class NotesServiceImpl implements  NotesService{
	
	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private FavouriteNotesRepository favouriteNotesRepo;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileRepository fileRepo;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		
		//converting json to string
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);
		
		if(!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto, file);
		}
		
	    // category validation
	    checkCategoryExist(notesDto.getCategory());
	    Notes notesMap = mapper.map(notesDto, Notes.class);  
	    FileDetails fileDtls =saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		}
		else {
			if(ObjectUtils.isEmpty(fileDtls)) {
				notesMap.setFileDetails(null);
			}		
		}
	    Notes saveNotes = notesRepo.save(notesMap);
	    if (!ObjectUtils.isEmpty(saveNotes)) {
	        return true;
	    }
	    return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception{
		Notes existNotes = notesRepo.findById(notesDto.getId())
				.orElseThrow(()-> new ResourceNotFoundException("Invalid notes id"));
		if(ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FilesDto.class));
		}	
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		
		if(!ObjectUtils.isEmpty(file) && ! file.isEmpty()) {
			
			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);

			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg","png", "docx", "txt");
			if (!extensionAllow.contains(extension)) {
			    throw new IllegalArgumentException("Invalid file format! Upload only .pdf, .xlsx, .jpg, .png, .docx, .txt");
			}		
			String rndString = UUID.randomUUID().toString();
			//String extension = FilenameUtils.getExtension(originalFileName);
			String uploadFileName = rndString+"."+extension;
				
			File saveFile = new File(uploadPath);
			if(!saveFile.exists()) {
				saveFile.mkdir();
			}
			//path : notenestapiservice/notes/java.pdf
			String storePath = uploadPath.concat(uploadFileName);
				
			//upload file 
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if(upload != 0) {
				FileDetails fileDlts = new FileDetails();
				String originalFileName = file.getOriginalFilename();
				fileDlts.setOriginalFileName(originalFileName);
				fileDlts.setDisplayFileName(getDisplayName(originalFileName));
				fileDlts.setUploadFileName(uploadFileName);
				fileDlts.setFileSize(file.getSize());
				fileDlts.setPath(storePath);
				FileDetails saveFileDlts = fileRepo.save(fileDlts);
				return saveFileDlts;
			}
		}
		return null;
	}

	private String getDisplayName(String originalFileName) {
		
		//extract file extension using apache common io
		String extension = FilenameUtils.getExtension(originalFileName);
		//get only the file name without the extension
		String fileName = FilenameUtils.removeExtension(originalFileName);
		//checking filename length if greater than 8 then substring to (0,7) and add the extension to it
		if(fileName.length()>8) {
			fileName = fileName.substring(0,7);
		}
		fileName= fileName+"."+extension;
		//return the file name
		return fileName;
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

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {
		
//		FileDetails fileDtls =   fileRepo.findById(id)
//				.orElseThrow(()->new ResourceNotFoundException("File is not available"));
		FileInputStream io = new FileInputStream(fileDetails.getPath());
		
		//convert InputStream object to byte array
		return StreamUtils.copyToByteArray(io);
	}
	
	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
	    FileDetails fileDtls = fileRepo.findById(id.longValue())
	        .orElseThrow(() -> new ResourceNotFoundException("File is not available"));
	    return fileDtls;
	}

	@Override
	public NotesResponse getAllNotesByUser( Integer pageNo, Integer pageSize) {
		
		// suppose i have 10 notes but 1 page can contain only 5 notes then it divides into 2 pages (5, 5)
		Integer userId = CommonUtil.getLoggedInUser().getId();	
		//implement pagination on notes
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes =  notesRepo.findByCreatedByAndIsDeletedFalse(userId,pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n,NotesDto.class)).toList();
		NotesResponse notes = NotesResponse.builder()
				.notes(notesDto)
				.pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize())
				.totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages())
				.isFirst(pageNotes.isFirst())
				.isLast(pageNotes.isLast())
				.build();
		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes id invalid! Not found"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepo.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes id invalid! Not found"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepo.save(notes);
		
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes() {		
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoList = recycleNotes.stream().map(note->mapper.map(note, NotesDto.class)).toList();
		return notesDtoList;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("notes not found"));
		if(notes.getIsDeleted()) {
			notesRepo.delete(notes);
		}else {
			throw new IllegalArgumentException("Sorry you can't hard delete directly");
		}
	}

	@Override
	public void emptyRecycleBin() throws Exception {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userId);
		if(!CollectionUtils.isEmpty(recycleNotes)) {
			notesRepo.deleteAll(recycleNotes);
		}
	}

	@Override
	public void favouriteNotes(Integer noteId) throws Exception{
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Notes notes = notesRepo.findById(noteId)
			.orElseThrow(()-> new ResourceNotFoundException("Notes not found! Id invalid"));
		FavouriteNotes favouriteNotes = FavouriteNotes.builder()
				.notes(notes)
				.userId(userId)
				.build();
		
		favouriteNotesRepo.save(favouriteNotes);
	}

	@Override
	public void unFavouriteNotes(Integer favouriteNoteId) throws Exception{
		FavouriteNotes favNote = favouriteNotesRepo.findById(favouriteNoteId)
				.orElseThrow(()-> new ResourceNotFoundException("Favourite Notes not found! Id invalid"));
		favouriteNotesRepo.delete(favNote);
	}
	
	@Override
	public List<FavouriteNotesDto> getUserFavoriteNotes() {
	   
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<FavouriteNotes> favouriteNotes = favouriteNotesRepo.findByUserId(userId);
		 return favouriteNotes.stream().map(fn->mapper.map(fn,FavouriteNotesDto.class)).toList();  
	}

	@Override
	public Boolean copyNotes(Integer id, Integer userId) throws Exception {
		Notes notes = notesRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Notes not found! Id invalid"));
		
		// Validation: Check if the note was created by the current user
		if (!notes.getCreatedBy().equals(userId)) {
		    throw new UnauthorizedException("You can only copy notes created by you");
		}
		Notes copyNote = Notes.builder()
				.title(notes.getTitle())
				.description(notes.getDescription())
				.category(notes.getCategory())
				.isDeleted(false)
				.fileDetails(null)
				.build();
		
		Notes saveCopyNote = notesRepo.save(copyNote);
		if(!ObjectUtils.isEmpty(saveCopyNote)) {
			return true;
		}
		return false;
	}

	@Override
	public byte[] exportNoteAsPdf(Integer noteId) throws Exception {
	    Notes note = notesRepo.findById(noteId)
	        .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));
	    
	    Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    try {
	        PdfWriter.getInstance(document, baos);
	        document.open();
	        
	        // Add note title as document title
	        Paragraph title = new Paragraph(note.getTitle());
	        title.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(title);
	        
	        document.add(new Paragraph(" ")); // Empty line
	        document.add(new Paragraph(" ")); // Another empty line
	        
	        // Add note content
	        Paragraph content = new Paragraph(note.getDescription());
	        document.add(content);
	        
	        document.add(new Paragraph(" ")); // Empty line
	        
	        // Add metadata
	        if (note.getCategory() != null) {
	            Paragraph category = new Paragraph("Category: " + note.getCategory().getName());
	            document.add(category);
	        }
	        
	        if (note.getCreatedOn() != null) {
	            Paragraph createdDate = new Paragraph("Created: " + note.getCreatedOn().toString());
	            document.add(createdDate);
	        }
	        
	        if (note.getUpdatedOn() != null) {
	            Paragraph updatedDate = new Paragraph("Last Updated: " + note.getUpdatedOn().toString());
	            document.add(updatedDate);
	        }
	        
	        // Add file attachment info if exists
	        if (note.getFileDetails() != null) {
	            document.add(new Paragraph(" "));
	            Paragraph fileInfo = new Paragraph("Attached File: " + note.getFileDetails().getOriginalFileName());
	            document.add(fileInfo);
	        }
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error generating PDF for note id: " + noteId, e);
	    } finally {
	        document.close();
	    }
	    return baos.toByteArray();
	}

	@Override
	public byte[] exportNoteAsDocx(Integer noteId) throws Exception {
	    Notes note = notesRepo.findById(noteId)
	        .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));
	    
	    try (XWPFDocument document = new XWPFDocument(); 
	         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	        
	        // Add title
	        XWPFParagraph titleParagraph = document.createParagraph();
	        XWPFRun titleRun = titleParagraph.createRun();
	        titleRun.setText(note.getTitle());
	        titleRun.setBold(true);
	        titleRun.setFontSize(18);
	        
	        // Add empty lines
	        document.createParagraph();
	        document.createParagraph();
	        
	        // Add content
	        XWPFParagraph contentParagraph = document.createParagraph();
	        XWPFRun contentRun = contentParagraph.createRun();
	        contentRun.setText(note.getDescription());
	        contentRun.setFontSize(12);
	        
	        // Add empty line
	        document.createParagraph();
	        
	        // Add metadata
	        XWPFParagraph metaParagraph = document.createParagraph();
	        XWPFRun metaRun = metaParagraph.createRun();
	        metaRun.setItalic(true);
	        metaRun.setFontSize(10);
	        
	        if (note.getCategory() != null) {
	            metaRun.setText("Category: " + note.getCategory().getName());
	            metaRun.addBreak();
	        }
	        
	        if (note.getCreatedOn() != null) {
	            metaRun.setText("Created: " + note.getCreatedOn().toString());
	            metaRun.addBreak();
	        }
	        
	        if (note.getUpdatedOn() != null) {
	            metaRun.setText("Last Updated: " + note.getUpdatedOn().toString());
	            metaRun.addBreak();
	        }
	        
	        // Add file attachment info if exists
	        if (note.getFileDetails() != null) {
	            metaRun.addBreak();
	            metaRun.setText("Attached File: " + note.getFileDetails().getOriginalFileName());
	        }
	        
	        document.write(baos);
	        return baos.toByteArray();
	    } catch (IOException e) {
	        throw new RuntimeException("Error generating DOCX for note id: " + noteId, e);
	    }
	}

	@Override
	public String getNoteFilename(Integer noteId, String extension) throws Exception {
	    Notes note = notesRepo.findById(noteId)
	        .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));
	    
	    return sanitizeFilename(note.getTitle()) + "." + extension;
	}

	private String sanitizeFilename(String filename) {
	    if (filename == null || filename.trim().isEmpty()) {
	        return "note";
	    }
	    // Remove invalid characters and limit length
	    String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
	    return sanitized.length() > 50 ? sanitized.substring(0, 50) : sanitized;
	}

	@Override
	public byte[] exportAllNotesAsPdf(Integer userId) throws Exception {
	    // Get all active notes for the user
	    List<Notes> notes = notesRepo.findByCreatedByAndIsDeletedFalse(userId);
	    
	    if (CollectionUtils.isEmpty(notes)) {
	        throw new ResourceNotFoundException("No notes found for user: " + userId);
	    }
	    
	    Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    try {
	        PdfWriter.getInstance(document, baos);
	        document.open();
	        
	        // Add document title
	        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
	        Paragraph documentTitle = new Paragraph("My Notes Collection", titleFont);
	        documentTitle.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(documentTitle);
	        
	        // Add export date
	        Font dateFont = new Font(Font.HELVETICA, 10, Font.ITALIC);
	        Paragraph exportDate = new Paragraph("Exported on: " + LocalDateTime.now().toString(), dateFont);
	        exportDate.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(exportDate);
	        
	        document.add(new Paragraph(" ")); // Empty line
	        document.add(new Paragraph(" ")); // Another empty line
	        
	        // Add notes count
	        Font countFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
	        Paragraph notesCount = new Paragraph("Total Notes: " + notes.size(), countFont);
	        document.add(notesCount);
	        
	        document.add(new Paragraph(" ")); // Empty line
	        
	        // Add a horizontal line separator
	        document.add(new Paragraph("_".repeat(80)));
	        document.add(new Paragraph(" "));
	        
	        // Group notes by category for better organization
	        Map<String, List<Notes>> notesByCategory = notes.stream()
	            .collect(Collectors.groupingBy(note -> 
	                note.getCategory() != null ? note.getCategory().getName() : "Uncategorized"
	            ));
	        
	        // Add each category section
	        Font categoryFont = new Font(Font.HELVETICA, 16, Font.BOLD);
	        Font noteTitleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
	        Font noteContentFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
	        Font metaFont = new Font(Font.HELVETICA, 9, Font.ITALIC);
	        
	        for (Map.Entry<String, List<Notes>> categoryEntry : notesByCategory.entrySet()) {
	            String categoryName = categoryEntry.getKey();
	            List<Notes> categoryNotes = categoryEntry.getValue();
	            
	            // Add category header
	            Paragraph categoryHeader = new Paragraph("Category: " + categoryName, categoryFont);
	            categoryHeader.setSpacingBefore(20);
	            categoryHeader.setSpacingAfter(10);
	            document.add(categoryHeader);
	            
	            // Add category separator
	            document.add(new Paragraph("-".repeat(60)));
	            document.add(new Paragraph(" "));
	            
	            // Sort notes by creation date (newest first)
	            categoryNotes.sort((n1, n2) -> n2.getCreatedOn().compareTo(n1.getCreatedOn()));
	            
	            // Add each note in the category
	            for (int i = 0; i < categoryNotes.size(); i++) {
	                Notes note = categoryNotes.get(i);
	                
	                // Add note number and title
	                Paragraph noteTitle = new Paragraph((i + 1) + ". " + note.getTitle(), noteTitleFont);
	                noteTitle.setSpacingBefore(15);
	                noteTitle.setSpacingAfter(5);
	                document.add(noteTitle);
	                
	                // Add note content
	                if (note.getDescription() != null && !note.getDescription().trim().isEmpty()) {
	                    Paragraph noteContent = new Paragraph(note.getDescription(), noteContentFont);
	                    noteContent.setSpacingAfter(8);
	                    noteContent.setIndentationLeft(20);
	                    document.add(noteContent);
	                }
	                
	                // Add metadata
	                StringBuilder metadata = new StringBuilder();
	                metadata.append("Created: ").append(formatDate(note.getCreatedOn()));
	                
	                if (note.getUpdatedOn() != null && !note.getUpdatedOn().equals(note.getCreatedOn())) {
	                    metadata.append(" | Updated: ").append(formatDate(note.getUpdatedOn()));
	                }
	                
	                if (note.getFileDetails() != null) {
	                    metadata.append(" | Attachment: ").append(note.getFileDetails().getOriginalFileName());
	                }
	                
	                Paragraph metaParagraph = new Paragraph(metadata.toString(), metaFont);
	                metaParagraph.setIndentationLeft(20);
	                metaParagraph.setSpacingAfter(5);
	                document.add(metaParagraph);
	                
	                // Add separator between notes
	                if (i < categoryNotes.size() - 1) {
	                    document.add(new Paragraph("• • •"));
	                }
	            }
	            
	            // Add space after category
	            document.add(new Paragraph(" "));
	        }
	        
	        // Add footer with total count
	        document.add(new Paragraph("_".repeat(80)));
	        document.add(new Paragraph(" "));
	        
	        Paragraph footer = new Paragraph("End of Notes Collection - Total: " + notes.size() + " notes", metaFont);
	        footer.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(footer);
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error generating PDF for all notes of user: " + userId, e);
	    } finally {
	        document.close();
	    }
	    
	    return baos.toByteArray();
	}

	// Helper method to format dates nicely
	private String formatDate(LocalDateTime dateTime) {
	    if (dateTime == null) return "N/A";
	    return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
	}

	// Overloaded method for Date objects (if your entity uses Date instead of LocalDateTime)
	private String formatDate(Date date) {
	    if (date == null) return "N/A";
	    
	    // Convert Date to LocalDateTime
	    LocalDateTime dateTime = date.toInstant()
	        .atZone(ZoneId.systemDefault())
	        .toLocalDateTime();
	    
	    return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
	}


	
	
	
}
