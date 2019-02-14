package main.java.com.idiban.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2503904231391341900L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
