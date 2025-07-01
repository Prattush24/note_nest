package com.priyanathbhukta.notenest.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.priyanathbhukta.notenest.entity.Category;
import com.priyanathbhukta.notenest.entity.FileDetails;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotesDto {
	
	private Integer id;
	
	private String title;
	
	private String description;
	
	private CategoryDto category;
	
	private Integer createdBy;
	
    private Date createdOn;
    
    private Integer updatedBy;
    
    private Date updatedOn;
    
    private FilesDto fileDetails;
    
	private Boolean isDeleted;
	
	private LocalDateTime deletedOn;
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    //implementing inner class 
    public static class FilesDto{
    	
    	private Integer id;
    	
    	private String originalFileName;
    	
    	private String displayFileName;

    }
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    //implementing inner class 
    public static class CategoryDto{
    	
    	private Integer id;
    	
    	private String name;
    }
}
