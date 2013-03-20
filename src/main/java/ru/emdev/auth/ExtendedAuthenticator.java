package ru.emdev.auth;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.emdev.auth.util.PropsKeys;
import ru.emdev.auth.util.SessionCountUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.service.UserLocalServiceUtil;

public class ExtendedAuthenticator implements Authenticator {

	private static final Log _log = LogFactoryUtil.getLog(ExtendedAuthenticator.class);

	@Override
	public int authenticateByEmailAddress(long companyId, String emailAddress, String password,
			Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
			throws AuthException {

		return authenticate(companyId, emailAddress, null, 0l);
	}

	@Override
	public int authenticateByScreenName(long companyId, String screenName, String password,
			Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
			throws AuthException {

		return authenticate(companyId, null, screenName, 0l);
	}

	@Override
	public int authenticateByUserId(long companyId, long userId, String password,
			Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
			throws AuthException {

		return authenticate(companyId, null, null, userId);
	}

	protected int authenticate(long companyId, String emailAddress, String screenName, long userId)
			throws AuthException {

		User user = null;
		try {
			if (userId > 0)
				user = UserLocalServiceUtil.getUser(userId);
			else if (StringUtils.isEmpty(emailAddress))
				user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
			else if (StringUtils.isEmpty(screenName))
				user = UserLocalServiceUtil.getUserByScreenName(companyId, screenName);
		} catch (Exception e) {
			_log.info("Can't find user", e);
		}

		if (user == null) {
			return DNE;
		} else {
			int maxCount = GetterUtil.getInteger(PropsUtil
					.get(PropsKeys.MAX_SESSION_COUNT_FOR_USER));

			if (SessionCountUtil.count(user.getUserId()) >= maxCount)
				throw new MaxSessionCountException();
		}

		return SUCCESS;
	}
}
