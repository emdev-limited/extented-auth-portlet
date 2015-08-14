package ru.emdev.security.auth;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.emdev.security.auth.util.ExtendedAuthSettingsUtil;
import ru.emdev.security.auth.util.PreferencesUtil;
import ru.emdev.security.auth.util.PropsKeys;
import ru.emdev.security.auth.util.SessionCountUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author Alexey Melnikov
 * @author Jose Campos
 *
 */
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

			//Access Portal Administrator
			if(ExtendedAuthSettingsUtil.hasAccessAdmin(companyId, usrId)){
				return SUCCESS;
			}
			
			int maxCount = ExtendedAuthSettingsUtil.getSessionCount(companyId, usrId);
			
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
			
			
			//IP Range Checking 
			boolean enabledOverride = PreferencesUtil.isEnabledOverrideUserSettings(companyId);

			if(enabledOverride && 
					ExtendedAuthSettingsUtil.getEmailDomains(companyId).length>0){
				// ip range check + domains
				 if(ExtendedAuthSettingsUtil.containsEmailAddress(companyId, user.getEmailAddress())){
					 ExtendedAuthSettingsUtil.checkIPRange(companyId, usrId);
				 }
			}else{
				// ip range check
				ExtendedAuthSettingsUtil.checkIPRange(companyId, usrId);
			}
			
			// check access dates
			if (ExtendedAuthSettingsUtil.isAccessByDateEnabled(companyId, usrId)) {
				Date from = ExtendedAuthSettingsUtil.getDateFrom(companyId, usrId);
				Date to = ExtendedAuthSettingsUtil.getDateTo(companyId, usrId);
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