package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer>  {
	
	UserToken findByUserId(String username);
	UserToken findByuserToken(String username);
	
}
