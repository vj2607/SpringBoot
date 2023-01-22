package com.example.demo.model;





import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "userId", nullable = false)
	private String userId;

	@Column(name = "usertoken", nullable = false)
	private String userToken;

	@Column(name = "TokenStatus", nullable = false)
	private String status;
	
	@Column(name = "tokenExpiration")
	private Date tokenExpiration;
	


}