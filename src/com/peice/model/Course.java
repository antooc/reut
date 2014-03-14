package com.peice.model;

public class Course {
	public Course(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isComplete = false;
	}
	public int id;
	public String name;
	public String description;
	public boolean isComplete;
}
