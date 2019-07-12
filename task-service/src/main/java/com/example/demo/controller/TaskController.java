package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<Task> addTasks(@RequestBody Task task) {
		Set<Category> categories = task.getCategories();
		Set<Category> taskCategories = new HashSet<>();
		categories.stream().forEach(category -> {
			Category existingCategory = categoryRepository.findByName(category.getName());
			if (existingCategory == null) {
				existingCategory = categoryRepository.save(category);
			}
			existingCategory.setTask(new HashSet<>());
			taskCategories.add(existingCategory);
		});
		task.setCategories(taskCategories);
		Task savedTask = taskRepository.save(task);
		return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
	}

	@GetMapping("user/{id}/tasks")
	public List<Task>userTasks(@PathVariable ("id") Long userId){
		return taskRepository.findByUserId(userId);
	}
}
