package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@RestResource
@Repository
public interface UserRespository extends JpaRepository<User, Long>{

}
