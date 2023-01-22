package com.example.demo.model;



import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateUser {

	@Column(name = "userId", nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;

}