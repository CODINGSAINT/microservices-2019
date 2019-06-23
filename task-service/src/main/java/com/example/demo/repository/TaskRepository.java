package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.demo.model.Task;

@RestResource
public interface TaskRepository extends JpaRepository<Task, Long>{

}
