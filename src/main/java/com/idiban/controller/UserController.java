package main.java.com.idiban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.idiban.datasource.DataSource;
import main.java.com.idiban.domain.LoginUser;
import main.java.com.idiban.domain.User;
import main.java.com.idiban.domain.UserLog;
import main.java.com.idiban.errors.UserNotFoundException;

@RestController
public class UserController {

	@Autowired
	private DataSource dataSource;

	@PostMapping("/register")
	public MappingJacksonValue addUser(@RequestBody User tempUser) {
		User newUser = dataSource.addUser(tempUser);

		if (newUser == null) {
			return dataSource.getMessage("ExistMail", "El Email ya existe");
		}

		UserLog newUserLog = dataSource.getLog(newUser);

		return dataSource.getCreatedMessage(newUserLog);
	}

	@GetMapping("/login")
	public MappingJacksonValue login(@RequestBody LoginUser loginUser) {

		MappingJacksonValue mappingJacksonValue = dataSource.login(loginUser);

		if (mappingJacksonValue == null) {
			throw new UserNotFoundException("Email: (" + loginUser.getEmail() + ") no se encuentra");
		}

		return mappingJacksonValue;

	}

	@GetMapping("/userprofile/{userid}")
	public MappingJacksonValue userProfile(@RequestHeader String token, @PathVariable Long userid) {

		Boolean tokenStatusBoolean = dataSource.checkToken(token);
		if (tokenStatusBoolean == false) {
			return dataSource.getMessage("No Token", "No autorizado");
		} else if (tokenStatusBoolean == true) {
			return dataSource.findUserById(userid, token);
		}

		return null;

	}

}
