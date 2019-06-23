package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.demo.model.Category;

@RestResource
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Category findByName(String name);
}
