package com.priyanathbhukta.notenest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//Response of /category api getcategorycategory
public class CategoryResponse {
	
	private Integer id;
	
	private String name;
	
	private String description;

}
