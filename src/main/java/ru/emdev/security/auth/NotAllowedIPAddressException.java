package ru.emdev.security.auth;

import com.liferay.portal.security.auth.AuthException;

/**
 * @author Alexey Melnikov
 */
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
