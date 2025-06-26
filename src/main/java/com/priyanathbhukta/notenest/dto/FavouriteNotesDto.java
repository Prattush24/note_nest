package com.priyanathbhukta.notenest.dto;

import com.priyanathbhukta.notenest.entity.Notes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteNotesDto {
	
	private Integer id;

	private NotesDto notes;
	
	private Integer userId;
}
