package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ChangePassword;
import com.example.demo.model.User;
import com.example.demo.model.UserDetail;
import com.example.demo.model.ValidateUser;
import com.example.demo.model.response.ValidateUserResponse;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {
	
	@Autowired 
	UserService userService;

//	@PostMapping("/users/register")
//	public ResponseEntity<String> register(@Valid @RequestBody User user) {
//	 //log.info(" request for Registration service");
//		
//		userService.registerUser(user);
//		return ResponseEntity.status(HttpStatus.OK)
//		        .body("User Created ");
//	}

	@GetMapping(path = "/users")
	public List<UserDetail> getAllUsers() {
		return userService.getAllUsers();
	}
	@GetMapping(path = "/users/{Username}")
	public UserDetail getUser(@Param("Username") String username) {
		return userService.getUser(username);
	}

	/*
	 * @GetMapping(path = "/users/?FirstName") public List<UserDetail>
	 * getUsersByParameter(@queryParam("FirstName")String
	 * FirstName,@QueryParam("LastName") String
	 * LastName,@PageableAsQueryParam("Status")String Status) { return
	 * userService.getUsersByParameter(FirstName,LastName,Status); }
	 */
	@PostMapping("/users/validate")
	public ResponseEntity<ValidateUserResponse> validateUser(@Valid @RequestBody ValidateUser user) {
	 log.info(" request for Registration service");
	 System.out.println("hhjk");
		
		ValidateUserResponse response = userService.validateUser(user,null);
		return ResponseEntity.status(HttpStatus.OK)
		        .body(response);
	}
	@PostMapping("/users/ChangePasswd")
	public ResponseEntity<ValidateUserResponse> changePasswd(@Valid @RequestBody ChangePassword user) {
	 log.info(" request for Registration service");
		
		ValidateUserResponse response = userService.changePassword(user);
		return ResponseEntity.status(HttpStatus.OK)
		        .body(response);
	}
}
