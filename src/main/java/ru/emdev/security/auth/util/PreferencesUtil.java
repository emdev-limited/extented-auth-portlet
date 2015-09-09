package ru.emdev.security.auth.util;

import java.util.Date;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * 
 * @author Jose Campos
 *
 */
public class PreferencesUtil {

	public static int getMaxCountByCompany(long companyId) {

		return getInteger(companyId, PropsKeys.MAX_SESSION_COUNT_FOR_USER, 0);

	}

	public static boolean isEnabledByCompany(long companyId) {

		return getBoolean(companyId,
				PropsKeys.EXTENDED_AUTH_ENABLED_BY_COMPANY, false);
	}

	public static boolean isEnabledOverrideUserSettings(long companyId) {

		return getBoolean(companyId,
				PropsKeys.EXTENDED_AUTH_ENABLED_OVERRIDE_USER_SETTINGS, false);
	}

	public static boolean isEnabledAccessAdmin(long companyId, long userId) {

		return getBoolean(companyId,
				PropsKeys.EXTENDED_AUTH_ENABLED_ACCESS_ADMIN, false);

	}

	public static String[] getAuthIPRange(long companyId) {

		return getStringArray(companyId, PropsKeys.EXTENDED_AUTH_IP_RANGE,
				StringPool.NEW_LINE);
	}

	public static String[] getAuthEmailDomains(long companyId) {

		return getStringArray(companyId, PropsKeys.EXTENDED_AUTH_EMAIL_DOMAINS,
				StringPool.NEW_LINE);

	}

	public static boolean isAccessByDateEnabled(long companyId) {
		
		return getBoolean(companyId, PropsKeys.EXTENDED_AUTH_ENABLED_DATE_RANGE, false);
		
	}

	public static Date getAccesDateFrom(long companyId) {

		return new Date(getLong(companyId, PropsKeys.EXTENDED_AUTH_ACCESS_DATE_FROM, 0));
		
	}
	
	public static Date getAccesDateTo(long companyId) {

		return new Date(getLong(companyId, PropsKeys.EXTENDED_AUTH_ACCESS_DATE_TO, 0));
		
	}
	
	private static boolean getBoolean(long companyId, String name,
			boolean defaultValue) {

		boolean result = false;
		try {

			result = PrefsPropsUtil.getBoolean(companyId, name, defaultValue);

		} catch (SystemException e) {
		}

		return result;
	}

	private static int getInteger(long companyId, String name, int defaultValue) {

		int result = 0;

		try {

			result = PrefsPropsUtil.getInteger(companyId, name, defaultValue);

		} catch (SystemException e) {
		}

		return result;
	}

	private static long getLong(long companyId, String name, int defaultValue) {

		long result = 0;

		try {

			result = PrefsPropsUtil.getLong(companyId, name, defaultValue);

		} catch (SystemException e) {
		}

		return result;
	}
	
	private static String[] getStringArray(long companyId, String name,
			String delimiter) {

		String[] result = null;

		try {
			result = PrefsPropsUtil.getStringArray(companyId, name, delimiter);
		} catch (SystemException e) {
		}

		return result;

	}

}
