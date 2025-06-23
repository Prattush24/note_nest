package com.priyanathbhukta.notenest.dto;

import java.util.Date;

import com.priyanathbhukta.notenest.entity.Category;


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
