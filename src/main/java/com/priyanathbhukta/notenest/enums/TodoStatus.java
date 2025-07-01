package com.priyanathbhukta.notenest.enums;

// why use enum instead of a table in database ? 
/*
 Answer => Though to store just the status using table in database making more complex the system, where as we use enum   
 		it is fixed having 3 status of the todo module : Not started, In progress, Completed
 */



public enum TodoStatus {
	
	NOT_STARTED(1, "Not Started"), IN_PROGRESS(2, "In Progress"), COMPLETED(3, "Completed");
	
	private Integer id;
	
	private String name;	
	
	TodoStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
