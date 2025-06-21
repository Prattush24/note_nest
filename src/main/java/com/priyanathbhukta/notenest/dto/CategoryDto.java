package com.priyanathbhukta.notenest.dto;

import java.util.Date;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer id;

//    @NotBlank(message = "Name must not be blank")
//    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    private String name;

//    @NotBlank(message = "Description must not be blank")
//    @Size(min = 10, max = 100, message = "Description must be between 10 and 100 characters")
    private String description;

//    @NotNull(message = "isActive must not be null")
    private Boolean isActive;

    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
}

