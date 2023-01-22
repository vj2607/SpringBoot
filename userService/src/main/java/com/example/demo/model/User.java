package com.example.demo.model;



import javax.persistence.Column;

import com.example.demo.util.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Column(name = "userId", nullable = false)
	private String username;

	@ValidPassword
	@Column(name = "password", nullable = false)
	private String password;

	private String firstName;

	private String lastName;

}