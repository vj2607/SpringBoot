package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserDetail;

@Repository
public interface UserRepository extends JpaRepository<UserDetail, Integer>  {
	
	UserDetail findByUsername(String username);
	List<UserDetail> findByFirstName(String FirstName);
	List<UserDetail> findByLastName(String LastName);
	List<UserDetail> findByFirstNameOrLastNameOrStatus(String FirstName,String LastName,String Status);
}
