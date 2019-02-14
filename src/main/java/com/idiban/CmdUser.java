package main.java.com.idiban;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import main.java.com.idiban.domain.Phone;
import main.java.com.idiban.domain.User;
import main.java.com.idiban.domain.UserLog;
import main.java.com.idiban.services.PhoneService;
import main.java.com.idiban.services.UserService;
import main.java.com.idiban.services.UserLogService;

@Component
public class CmdUser implements CommandLineRunner {

	@Autowired
	private UserService userService;
	@Autowired
	private PhoneService phoneService;
	@Autowired
	private UserLogService userLogService;

	@Override
	public void run(String... args) throws Exception {

		User newUserOne = new User("idiban", "ijdkhv@gmail.com", "111");
		User addedUserOne = userService.save(newUserOne);
		System.out.println("Added User ------------> " + addedUserOne.toString());
		Phone phone = new Phone("56", "987877976");
		phone.setUser(newUserOne);
		phoneService.save(phone);
		UUID uuidOne = UUID.randomUUID();
		UserLog userLogOne = new UserLog(newUserOne.getId(), LocalDateTime.now(), uuidOne.toString());
		userLogOne.setUserTwo(newUserOne);
		userLogService.save(userLogOne);

		User newUserTwo = new User("idiban1", "ijdkhv1@gmail.com", "222");
		User addedUserTwo = userService.save(newUserTwo);
		System.out.println("Added User ------------> " + addedUserTwo.toString());
		Phone phoneTwo = new Phone("56", "985878976");
		phoneTwo.setUser(newUserTwo);
		phoneService.save(phoneTwo);
		UUID uuidTwo = UUID.randomUUID();
		UserLog userLogTwo = new UserLog(newUserTwo.getId(), LocalDateTime.now(), uuidTwo.toString());
		userLogTwo.setUserTwo(newUserTwo);
		userLogService.save(userLogTwo);

		User newUserThree = new User("idiban2", "ijdkhv2@gmail.com", "333");
		User addedUserThree = userService.save(newUserThree);
		System.out.println("Added User ------------> " + addedUserThree.toString());
		Phone phoneThree = new Phone("56", "987898976");
		phoneThree.setUser(newUserThree);
		phoneService.save(phoneThree);
		UUID uuidThree = UUID.randomUUID();
		UserLog userLogThree = new UserLog(newUserThree.getId(), LocalDateTime.now(), uuidThree.toString());
		userLogThree.setUserTwo(newUserThree);
		userLogService.save(userLogThree);

	}

}
