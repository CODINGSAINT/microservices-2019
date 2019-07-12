package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "task")
@Data

public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Long taskId;
	private Long userId;
	private String name;
	private String desc;
	private Boolean isDone;
	private Date targetDate;
	@ManyToMany(cascade = { CascadeType.PERSIST,
			CascadeType.MERGE},
            fetch = FetchType.LAZY	)
	  @JoinTable(name = "task_catogory",
      joinColumns = { @JoinColumn(name = "task_id") },
      inverseJoinColumns = { @JoinColumn(name = "category_id") })
	  private Set<Category> categories= new HashSet<>();

}
