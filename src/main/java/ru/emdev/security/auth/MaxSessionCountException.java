package ru.emdev.security.auth;

import com.liferay.portal.security.auth.AuthException;

/**
 * @author Alexey Melnikov
 */
public class MaxSessionCountException extends AuthException {

	public MaxSessionCountException() {
		super();
	}

	public MaxSessionCountException(String msg) {
		super(msg);
	}

	public MaxSessionCountException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MaxSessionCountException(Throwable cause) {
		super(cause);
	}
}
