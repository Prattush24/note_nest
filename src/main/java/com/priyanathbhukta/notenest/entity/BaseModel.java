package com.priyanathbhukta.notenest.entity;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    
    private String description;
    
    private Boolean isActive;
    
    private Boolean isDeleted;
    
    private Integer createdBy;
    
    private Date createdOn;
    
    private Integer updatedBy;
    
    private Date updatedOn;
}