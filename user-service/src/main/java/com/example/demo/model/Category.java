package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Category {
	private Long categoryId;
	private String name;
	@JsonIgnore
	  private Set<Task> task= new HashSet<>();
	

}
