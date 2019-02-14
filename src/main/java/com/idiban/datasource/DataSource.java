package main.java.com.idiban.datasource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import main.java.com.idiban.PassHash;
import main.java.com.idiban.domain.LoginUser;
import main.java.com.idiban.domain.Phone;
import main.java.com.idiban.domain.User;
import main.java.com.idiban.domain.UserLog;
import main.java.com.idiban.errors.Error;
import main.java.com.idiban.errors.NotAuthorizedException;
import main.java.com.idiban.errors.UserNotFoundException;
import main.java.com.idiban.services.PhoneService;
import main.java.com.idiban.services.UserService;
import main.java.com.idiban.services.UserLogService;


@Component
public class DataSource {

//	private static List<User> userList = new ArrayList<>();
//	private static List<UserLog> userListLog = new ArrayList<>();

	@Autowired
	private UserService userInterface;
	@Autowired
	private PhoneService phoneInterface;
	@Autowired
	private UserLogService userLogInterface;

	// add user to the list
	public User addUser(User tempUser) {
		if (!checkMail(tempUser)) {

			// adding user to the userList
			userInterface.save(tempUser);
			// adding list of phones to the phone table
			List<Phone> phones = tempUser.getPhones();

			for (Phone tempPhone : phones) {
				tempPhone.setUser(tempUser);
				phoneInterface.save(tempPhone);
			}

			// adding log for user in log table
			UUID uuid = UUID.randomUUID();
			UserLog userLog = new UserLog(tempUser.getId(), LocalDateTime.now(), uuid.toString());
			userLog.setUser2(tempUser);
			userLogInterface.save(userLog);

			return tempUser;

		} else {
			return null;
		}
	}

	// get all users
	public List<User> getAllUsers() {
		return userInterface.findAll();
	}

	// get all logs
	public List<UserLog> getAllLogs() {
		return userLogInterface.findAll();
	}

	// get all phones
	public List<Phone> getAllPhones() {
		return phoneInterface.findAll();
	}

	// to check email andpassword - login
	public MappingJacksonValue login(LoginUser loginUser) {

		for (User user : userInterface.findAll()) {
			if (user.getEmail().equals(loginUser.getEmail())) {
				if (PassHash.getInstance().toComparePasswords(loginUser.getPassword(),
						user.getPassword())) {
					// loginSuccess
					UserLog userLog = getLog(user);
					return getCreatedMessage(userLog);
				} else {
					// email exists ,but password doesn't match
					throw new NotAuthorizedException("Usu�rio e/ou senha inv�lidos");
				}
			} else {
				// nada
			}
		}
		// email doesn't exists - Usu�rio e/ou senha inv�lidos
		throw new UserNotFoundException("Usu�rio e/ou senha inv�lidos");
	}

	// get log with id
	public UserLog getLog(User tempUser) {

		for (UserLog userLog : userLogInterface.findAll()) {
			if (userLog.getId() == tempUser.getId()) {
				// to update last login field
				userLog.setLastLoginDate(LocalDateTime.now());
				userLogInterface.save(userLog);

				return userLog;
			}
		}
		return null;
	}

	// to check if email is in list or not
	public boolean checkMail(User tempUser) {

		for (User user : userInterface.findAll()) {
			if (user.getEmail().equals(tempUser.getEmail())) {
				return true;
			}
		}
		return false;
	}

	// getMessage
	public MappingJacksonValue getMessage(String messageName, String messageDescription) {

		Error errorStructure = new Error(messageName, messageDescription);
		// ************************************************
		// adding filter for the fields that will be shown
		// ************************************************

		// making a filter and filtering ALL except,,,,
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("name",
				"details");

		// creating a filter provider
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("ErrorStructure",
				simpleBeanPropertyFilter);

		// creating a map....
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(errorStructure);

		// Settings theSettings filter
		mappingJacksonValue.setFilters(filterProvider);

		// ************************************************
		// ************************************************

		// returning the defined mapp as return type
		return mappingJacksonValue;
	}

	
	// to get created message
	public MappingJacksonValue getCreatedMessage(UserLog tempUserLog) {

		// ************************************************
		// adding filter for the fields that will be shown
		// ************************************************

		// making a filter and filtering ALL except,,,,
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id",
				"createdDate", "modifiedDate", "lastLoginDate", "token");

		// creating a filter provider
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserLogFilter", simpleBeanPropertyFilter);

		// creating a map....
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(tempUserLog);

		// Settings theSettings filter
		mappingJacksonValue.setFilters(filterProvider);

		// ************************************************
		// ************************************************

		// returning the defined mapp as return type
		return mappingJacksonValue;

	}

	// to check if oken exists in list or not
	public Boolean checkToken(String token) {

		for (UserLog userLog : userLogInterface.findAll()) {
			if (userLog.getToken().equals(token)) {
				return true;
			}
		}

		return false;
	}

	// to find user by id
	public MappingJacksonValue findUserById(Long userid, String token) {

		Optional<User> findedUserOptional = null;

		try {
//			findedUserOptional = userInterface.findById(userid);

			User tempUser = findedUserOptional.get();

			if (tempUser != null) {
				// entered user id exists in the list
				return checkTokenWithUserToken(tempUser, token);
			} else {
				// entered user id doesn't exists in the list
				return null;
//				return getMessage("InvalidUserID", "User Id Not Found");
			}

		} catch (Exception e) {
			// entered user id doesn't exists in the list
			throw new UserNotFoundException("Usu�rio inv�lido");
		}

	}

	// checking inserted token with user's token
	public MappingJacksonValue checkTokenWithUserToken(User user, String token) {

		if (user.getUserLog().getToken().equals(token)) {
			// entered token matched with users token
			return checkLastLogin(user);
		} else {
			// entered token does't match with user's token
			return getMessage("TokenNotMatchWithUser", "N�o autorizado");
		}

	}

	// to check the last login
	public MappingJacksonValue checkLastLogin(User user) {

		LocalDateTime localDateTime = LocalDateTime.now();
		Duration duration = Duration.between(localDateTime, user.getUserLog().getLastLoginDate());
		long dur = duration.toMinutes();

		if (dur < 31) {
			// loginSuccess
			UserLog userLog = getLog(user);
			return getCreatedMessage(userLog);

		} else {
			return getMessage("Session Invalid", "Sess�o inv�lida");
		}

	}
	
}
