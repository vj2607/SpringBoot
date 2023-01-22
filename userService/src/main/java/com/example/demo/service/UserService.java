package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.ChangePassword;
import com.example.demo.model.LoginHistory;
import com.example.demo.model.User;
import com.example.demo.model.UserDetail;
import com.example.demo.model.UserPassword;
import com.example.demo.model.UserToken;
import com.example.demo.model.ValidateUser;
import com.example.demo.model.response.ValidateUserResponse;
import com.example.demo.repository.LoginHistoryRepository;
import com.example.demo.repository.UserPasswordRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserTokenRepository userTokenRepository;

	@Autowired
	UserPasswordRepo userPasswdRepo;

	@Autowired
	LoginHistoryRepository loginHistoryRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Autowired
	private PasswordEncoder encoder;

	public UserDetail registerUser(User user) throws BadRequestException {

		//log.info(" registerUser user");
		UserDetail userDetail = new UserDetail();
		String password = user.getPassword();
		if (password.isEmpty()) {
			throw new BadRequestException("Invalid password.");
		}

		//log.info(" encrypt passwd  user1");
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(30);
		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		
		//log.info(" setting user details");
		userDetail.setUsername(user.getUsername());
		userDetail.setFirstName(user.getFirstName());
		userDetail.setLastName(user.getLastName());
		userDetail.setPassword(encodedPassword);
		userDetail.setStatus("Active");
		userDetail.setInvalidAttempt(0);
		
		//log.info(" verify if user exist " + userDetail.getUsername());
	
		UserDetail userExists = userRepository.findByUsername(userDetail.getUsername());

		if (userExists != null) {
			//log.info(" user not exist " + userDetail.getUsername());
			throw new BadRequestException(user.getUsername() + " already registered.");
		}

		
		
	//	log.info(" save user details");
		userRepository.save(userDetail);
		
		savePassword(user.getUsername(),encodedPassword);
		
		

	//	log.info("User details updated");

		return userExists;
	}

	private void savePassword(String username, String encodedPassword) {
		log.info(" save Passwd details ");
		UserPassword uc = new UserPassword();
		uc.setUserId(username);
		uc.setPassWord(encodedPassword);
		userPasswdRepo.save(uc);
		
	}

	public List<UserDetail> getAllUsers() {
		List<UserDetail> userExists = userRepository.findAll();
		for (UserDetail user : userExists) {
			user.setPassword("");
		}
		return userExists;
	}
	public UserDetail getUser(String username)  throws BadRequestException {
		
		UserDetail userExists = userRepository.findByUsername(username);
		
		if (userExists == null) {
			
			throw new BadRequestException( "user not exist");
		}
		
			userExists.setPassword("");
		
		return userExists;
	}
		public List<UserDetail> getUsersByParameter(String FirstName,String LastName,String Status) {
		List<UserDetail> userExists = userRepository.findByFirstNameOrLastNameOrStatus( FirstName,  LastName, Status);
		if (FirstName.isEmpty()) {
			throw new BadRequestException("Invalid password.");
		}
		for (UserDetail user : userExists) {
			user.setPassword("");
		}
		return userExists;
	}
	
	
	public ValidateUserResponse validateUser(ValidateUser user,String newPassword) throws BadRequestException {
		
		log.info(" verify if user exist " + user.getUsername());
		String password = user.getPassword();
		if (password.isEmpty()) {
			throw new BadRequestException("Invalid password.");
		}
		UserDetail userExists = userRepository.findByUsername(user.getUsername());
		userExists.setLastloginTime(null);
		if (userExists == null) {
			log.info(" user not exist " + user.getUsername());
			throw new BadRequestException(user.getUsername() + "user not exist");
		}
		
		if (!encoder.matches(password, userExists.getPassword()) && "Active".equalsIgnoreCase(userExists.getStatus())) {
			userExists.setInvalidAttempt(userExists.getInvalidAttempt()+1);
			if(userExists.getInvalidAttempt()>5)
				userExists.setStatus("Inactive");
			
			log.info(" update user details on invalid login");
			userRepository.save(userExists);
			if ("Active".equalsIgnoreCase(userExists.getStatus()))
				throw new BadRequestException("Invalid userExistsuser name and password combination.");
			else
				throw new BadRequestException("User is inActive contact Admin");
		}
		
		if(newPassword!=null) 
		 validateNewPassword(userExists,newPassword);
		
		userExists.setInvalidAttempt(0);
		userRepository.save(userExists);
        String token = createUpdateToken(userExists);
		
		updateloginHistory(userExists);		
		

		return new ValidateUserResponse("Success",token);
	}

	private void validateNewPassword(UserDetail userExists, String newPassword) {
		log.info("size of result = {}  result = {}",userPasswdRepo.findAllByuserId(userExists.getUsername()).size(),userPasswdRepo.findAllByuserId(userExists.getUsername()));
		Optional<String> result = userPasswdRepo.findAllByuserId(userExists.getUsername()).subList(0, 4).stream()
				.filter(e -> encoder.matches(newPassword, e)).findAny();
		if(result.isPresent())
			throw new BadRequestException("New passwd matches with 5 old passwd");
		userExists.setPassword(encoder.encode(newPassword));
		savePassword(userExists.getUsername(),userExists.getPassword());
	}

	private void updateloginHistory(UserDetail userExists) {
		log.info("save login history");
		LoginHistory loginHistory = new LoginHistory();
		loginHistory.setUserId(userExists.getUsername());
		loginHistoryRepository.save(loginHistory);

		log.info("User details updated");
		
	}

	private String createUpdateToken(UserDetail userExists) {
		log.info(" save token details ");
		UserToken token = new UserToken();
		token.setStatus("Active");
		token.setUserId(userExists.getUsername());
		token.setUserToken(UUID.randomUUID().toString());
		LocalDateTime dateTime = LocalDateTime.now().plusMinutes(10);
		token.setTokenExpiration(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
		userTokenRepository.save(token);
		return token.getUserToken();
		
	}

	public ValidateUserResponse changePassword(@Valid ChangePassword changeUserPasswd) {
		ValidateUser user=new ValidateUser ();
		user.setPassword(changeUserPasswd.getOldPassword());
		user.setUsername(changeUserPasswd.getUsername());
		log.info("user details {} ",user);
		return validateUser(user,changeUserPasswd.getNewPassword());
	}

}
