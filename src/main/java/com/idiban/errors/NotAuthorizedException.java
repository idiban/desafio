package main.java.com.idiban.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends RuntimeException {


	private static final long serialVersionUID = -3686101872548268991L;

	public NotAuthorizedException(String message) {
		super(message);
	}

}
