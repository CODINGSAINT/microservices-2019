package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Task {

	private Long taskId;
	private Long userId;
	private String name;
	private String desc;
	private Boolean isDone;
	private Date targetDate;

	private Set<Category> categories = new HashSet<>();
}
