package ru.emdev.security.auth;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.emdev.security.auth.util.ExpandoUtil;
import ru.emdev.security.auth.util.PropsKeys;
import ru.emdev.security.auth.util.SessionCountUtil;
import ru.emdev.security.auth.util.net.IPUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
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
			_log.info("Can't find user: " + e.getMessage());
		}

		if (user == null) {
			return DNE;
		} else {
			int maxCount = GetterUtil.getInteger(PropsUtil
					.get(PropsKeys.MAX_SESSION_COUNT_FOR_USER));

			// max session check (ДЕРЕВО)
			long usrId = user.getUserId();
			if (maxCount != 0 && SessionCountUtil.count(usrId) >= maxCount)
				throw new MaxSessionCountException();

			// ip range check if specified for user
			List<String[]> allowedUserIPs = ExpandoUtil.getAllowedUserIP(companyId, usrId);
			String ip = user.getLastLoginIP();
			for (String[] allowedIP : allowedUserIPs) {

				boolean contains = false;
				try {
					int length = allowedIP.length;
					contains = (length == 1 && IPUtil.rangeContains(allowedIP[0], ip))
							|| (length == 2 && IPUtil.rangeContains(allowedIP[0], allowedIP[1], ip));
				} catch (Exception e) {
					_log.error("Skipped IP address/range[" + StringUtil.merge(allowedIP)
							+ "] check because error occured", e);
				}
				if (!contains) {
					_log.info("User[" + usrId + "] can't login because his address[" + ip
							+ "] is not allowed in settings.");

					throw new NotAllowedIPAddressException();
				}
			}

			// check access dates
			if (ExpandoUtil.isAccessByDateEnabled(companyId, usrId)) {
				Date from = ExpandoUtil.getDateFrom(companyId, usrId);
				Date to = ExpandoUtil.getDateTo(companyId, usrId);
				Date now = DateUtil.newDate();

				if ( !(from == null || now.after(from) && to == null || now.before(to)) )
					_log.info("User[" + usrId + "] can't login because his usage period expired .");
					throw new NonWorkingTimeException();
			}
		}

		return SUCCESS;
	}
}