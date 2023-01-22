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
public class ChangePassword {

	@Column(name = "userId", nullable = false)
	private String username;
	
	@Column(name = "oldPassword", nullable = false)
	private String oldPassword;
	@ValidPassword
	@Column(name = "newPassword", nullable = false)
	private String newPassword;

	

}