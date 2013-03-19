package ru.emdev.auth;

import com.liferay.portal.security.auth.AuthException;

public class NotAllowedIPAddressException extends AuthException {

	public NotAllowedIPAddressException() {
		super();
	}

	public NotAllowedIPAddressException(String msg) {
		super(msg);
	}

	public NotAllowedIPAddressException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NotAllowedIPAddressException(Throwable cause) {
		super(cause);
	}
}
