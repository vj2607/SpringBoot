package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserPassword;



@Repository
public interface UserPasswordRepo extends JpaRepository<UserPassword, Integer>  {
	
	UserPassword findByUserId(String username);
	 @Query ("SELECT passWord FROM UserPassword WHERE userId =?1 order by createDate desc")
    //@Query ("SELECT passWord FROM UserPassword")
	List<String> findAllByuserId(String username);
}
