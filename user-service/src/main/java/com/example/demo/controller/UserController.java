package com.example.demo.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.model.UserTask;
import com.example.demo.repository.UserRespository;
import com.example.demo.service.TaskService;


@RestController
@RequestMapping("v1")
public class UserController {
	 Logger logger = LoggerFactory.getLogger(UserController.class);
	 
	private UserRespository userRepository;
	private RestTemplate restTemplate;
private TaskService taskService;
	UserController(UserRespository userRespository, RestTemplate restTemplate,TaskService taskService) {
		this.userRepository = userRespository;
		this.restTemplate = restTemplate;
		this.taskService=taskService;
	}

	@PostMapping("user")
	public User user(@RequestBody User user) {
		logger.info("New User :{}",user);
		return userRepository.save(user);
	}

	@GetMapping("users")
	public Collection<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("user/{id}")
	public Optional<User> getUser(@PathVariable("id") Long id) {
		logger.info("User id:{}",id);
		
		return userRepository.findById(id);
	}

	@GetMapping("user/{id}/tasks")
	public ResponseEntity<?> userTasks(@PathVariable("id") Long id) {
		Optional<User> user = getUser(id);
		if (user.isPresent()) {
			logger.info("User found :{}",user);
			
		/*	ResponseEntity<List<Task>> tasks = restTemplate.exchange("http://localhost:8083/user/" + id + "/tasks",
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>() {
					});
					*/
			ResponseEntity<List<Task>> tasks=taskService.userTasks(id);
			logger.info("User tasks :{}",tasks.getBody());
			
			return new ResponseEntity<UserTask>(new UserTask(user.get(), tasks.getBody()), HttpStatus.OK);
		}
		else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

	}
}
