package com.priyanathbhukta.notenest.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.catalina.mapper.Mapper;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.priyanathbhukta.notenest.dto.NotesDto;
import com.priyanathbhukta.notenest.dto.NotesDto.CategoryDto;
import com.priyanathbhukta.notenest.dto.NotesResponse;
import com.priyanathbhukta.notenest.entity.FileDetails;
import com.priyanathbhukta.notenest.entity.Notes;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.repository.CategoryRepository;
import com.priyanathbhukta.notenest.repository.FileRepository;
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
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileRepository fileRepo;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		
		//converting json to string
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
	
	    // category validation
	    checkCategoryExist(notesDto.getCategory());

	    Notes notesMap = mapper.map(notesDto, Notes.class);
	    
	    FileDetails fileDtls =saveFileDetails(file);
		if(!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		}
		else {
			notesMap.setFileDetails(null);
		}
	    Notes saveNotes = notesRepo.save(notesMap);

	    if (!ObjectUtils.isEmpty(saveNotes)) {
	        return true;
	    }

	    return false;
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
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
		
		// suppose i have 10 notes but 1 page can contain only 5 notes then it divides into 2 pages (5, 5)
		
			
		//implement pagination on notes
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes =  notesRepo.findByCreatedBy(userId,pageable);
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
	
}
