package ru.emdev.security.auth;

import com.liferay.portal.security.auth.AuthException;

public class NonWorkingTimeException extends AuthException {

	public NonWorkingTimeException() {
		super();
	}

	public NonWorkingTimeException(String msg) {
		super(msg);
	}

	public NonWorkingTimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NonWorkingTimeException(Throwable cause) {
		super(cause);
	}
}
