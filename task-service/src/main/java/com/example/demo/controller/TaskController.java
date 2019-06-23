package com.example.demo.controller;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.model.Task;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TaskRepository;

@RestController
public class TaskController {

	private TaskRepository taskRepository;
	private CategoryRepository categoryRepository;

	public TaskController(TaskRepository taskRepository, CategoryRepository categoryRepository) {
		this.taskRepository = taskRepository;
		this.categoryRepository = categoryRepository;
	}

	@PostMapping("task")

	@Transactional
	public ResponseEntity<?> addTasks(@RequestBody Task task) {
		Set<Category> categories = task.getCategories();
		Set<Category> userCategories = new HashSet<>();
		categories.stream().forEach(category -> {
			Category existingCategory = categoryRepository.findByName(category.getName());
			if (existingCategory == null) {
				existingCategory = categoryRepository.save(category);

			}
			existingCategory.setTask(new HashSet<>());
			userCategories.add(existingCategory);
		});
		task.setCategories(userCategories);
		Task savedTask = taskRepository.save(task);
		return new ResponseEntity(savedTask, HttpStatus.CREATED);

	}

}
