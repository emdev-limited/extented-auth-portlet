package ru.emdev.security.auth;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.emdev.security.auth.util.ExpandoUtil;
import ru.emdev.security.auth.util.PropsKeys;
import ru.emdev.security.auth.util.SessionCountUtil;
import ru.emdev.security.auth.util.net.IPUtil;

import com.liferay.portal.kernel.audit.AuditRequestThreadLocal;
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

	protected int authenticate(long companyId, String emailAddress, String screenName, final long userId)
			throws AuthException {

		User user = null;
		try {
			if (userId > 0)
				user = UserLocalServiceUtil.getUser(userId);
			else if (!StringUtils.isEmpty(emailAddress))
				user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
			else if (!StringUtils.isEmpty(screenName))
				user = UserLocalServiceUtil.getUserByScreenName(companyId, screenName);
		} catch (Exception e) {
			_log.info("Can't find user: " + e.getMessage());
		}

		if (user == null) {
			return DNE;
		} else {
			long usrId = user.getUserId();

			int maxCount = ExpandoUtil.getSessionCount(companyId, usrId);

			if (maxCount == 0) {
				maxCount = GetterUtil.getInteger(PropsUtil
						.get(PropsKeys.MAX_SESSION_COUNT_FOR_USER));
				_log.debug("Global maximum session count for users is: " + maxCount);
			}
			else
				_log.debug("Maximum session count for user is: " + maxCount);
			// max session check (ДЕРЕВО)
			if (maxCount != 0 && SessionCountUtil.count(usrId) >= maxCount)
				throw new MaxSessionCountException();

			// ip range check if specified for user
			List<String[]> allowedUserIPs = ExpandoUtil.getAllowedUserIP(companyId, usrId);
			
			if(allowedUserIPs.size()>0){
				
				String ip = AuditRequestThreadLocal.getAuditThreadLocal().getClientIP();
				_log.debug("User IP is: " + ip);
			
				boolean contains = false;
				for (Iterator<String[]> iterator = allowedUserIPs.iterator(); 
						(iterator.hasNext() && !contains);) {
					
					String[] allowedIP = (String[]) iterator.next();
					
					_log.debug("Check IP address range: " + StringUtil.merge(allowedIP));
					
					try {
						int length = allowedIP.length;
						contains = (length == 1 && IPUtil.rangeContains(allowedIP[0], ip))
								|| (length == 2 && IPUtil.rangeContains(allowedIP[0], allowedIP[1], ip));
					} catch (Exception e) {
						_log.error("Skipped IP address/range[" + StringUtil.merge(allowedIP)
								+ "] check because error occured", e);
					}
				}
				
				if (!contains) {
					_log.info("User[" + usrId + "] can't login because his IP address[" + ip
							+ "] is not allowed in settings.");

					throw new NotAllowedIPAddressException();
				}
			}

			// check access dates
			if (ExpandoUtil.isAccessByDateEnabled(companyId, usrId)) {
				Date from = ExpandoUtil.getDateFrom(companyId, usrId);
				Date to = ExpandoUtil.getDateTo(companyId, usrId);
				Date now = DateUtil.newDate();

				_log.debug("Checking access from date: " + from.toString());
				_log.debug("Checking access to date: " + to.toString());
				_log.debug("Now date: " + now.toString());
				if ( !(
						(from == null || now.after(from)) && 
						(to == null || now.before(to))
					  )
				) {
					_log.info("User[" + usrId + "] can't login because his usage period expired .");
					throw new NonWorkingTimeException();
				}
			}
		}

		return SUCCESS;
	}
}