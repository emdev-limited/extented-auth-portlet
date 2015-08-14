package ru.emdev.security.auth.util;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import ru.emdev.security.auth.NotAllowedIPAddressException;
import ru.emdev.security.auth.util.net.IPUtil;

import com.liferay.portal.kernel.audit.AuditRequestThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.service.RoleLocalServiceUtil;

/**
 * 
 * @author Jose Campos
 *
 */
public class ExtendedAuthSettingsUtil {

	public static int getSessionCount(long companyId, long userId) {

		boolean enabledByCompany = PreferencesUtil
				.isEnabledByCompany(companyId);

		boolean enabledOverride = PreferencesUtil
				.isEnabledOverrideUserSettings(companyId);

		int maxCount = ExpandoUtil.getSessionCount(companyId, userId);

		int companyMaxCount = PreferencesUtil.getMaxCountByCompany(companyId);

		if (enabledByCompany && enabledOverride) {
			maxCount = companyMaxCount;
		}

		return maxCount;
	}

	public static List<String[]> getAllowedUserIP(long companyId, long userId) {

		boolean enabledByCompany = PreferencesUtil.isEnabledByCompany(companyId);
		boolean enabledOverride = PreferencesUtil.isEnabledOverrideUserSettings(companyId);

		List<String[]> ipRanges = ExpandoUtil.getAllowedUserIP(companyId,userId);
		List<String[]> companyIPRanges = ExtendedAuthSettingsUtil.getAllowedCompanyIP(companyId);

		if (enabledByCompany) {
			_log.debug("Enabled extended authentication at comapany level");
			if (ipRanges.size() == 0) {
				_log.debug("User does not IP rules settings, applying company settings");
				ipRanges = companyIPRanges;
			}
		}

		if (enabledOverride) {
			_log.debug("Enabled override, applying company settings");
			ipRanges = companyIPRanges;
		}
		
		return ipRanges;
	}

	public static String[] getEmailDomains(long companyId) {

		return PreferencesUtil.getAuthEmailDomains(companyId);

	}

	public static boolean containsEmailAddress(long companyId,
			String emailAddress) {
		String[] domains = getEmailDomains(companyId);

		return ArrayUtils.contains(domains,
				emailAddress.split(StringPool.AT)[1]);

	}

	private static List<String[]> getAllowedCompanyIP(long companyId) {

		return convertIPRanges(PreferencesUtil.getAuthIPRange(companyId));

	}

	public static List<String[]> convertIPRanges(String[] ipRanges) {

		List<String[]> result = new LinkedList<String[]>();
		for (String ip : ipRanges) {
			if (StringUtils.isBlank(ip))
				continue;
			if (ip.contains("-")) {
				String[] range = ip.split("-");
				if (StringUtils.isNotBlank(range[0])
						&& StringUtils.isNotBlank(range[1])) {
					range[0] = range[0].trim();
					range[1] = range[1].trim();
					result.add(range);
				}
			}
			result.add(new String[] { ip.trim() });
		}

		return result;
	}

	

	public static boolean hasAccessAdmin(long companyId, long userId) {

		try {
			if (PreferencesUtil.isEnabledAccessAdmin(companyId, userId)
					&& RoleLocalServiceUtil.hasUserRole(userId, companyId,
							RoleConstants.ADMINISTRATOR, true)) {
				return true;
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return false;
	}


	public static boolean isAccessByDateEnabled(long companyId, long userId) {

		boolean enabled = ExpandoUtil.isAccessByDateEnabled(companyId, userId);
		
		boolean companyEnableDate = PreferencesUtil.isAccessByDateEnabled(companyId);
		boolean enabledByCompany = PreferencesUtil.isEnabledByCompany(companyId);
		boolean enabledOverride = PreferencesUtil.isEnabledOverrideUserSettings(companyId);

		if(enabledByCompany && enabledOverride){
			enabled = companyEnableDate;
		}
		
		return enabled;
	}
	
	

	public static Date getDateFrom(long companyId, long userId) {
		
		boolean enabledUser = ExpandoUtil.isAccessByDateEnabled(companyId, userId);
		boolean companyEnableDate = PreferencesUtil.isAccessByDateEnabled(companyId);
		boolean enabledByCompany = PreferencesUtil.isEnabledByCompany(companyId);
		boolean enabledOverride = PreferencesUtil.isEnabledOverrideUserSettings(companyId);
		
		Date dateFrom = null; 
		Date companyDateFrom = PreferencesUtil.getAccesDateFrom(companyId);
		
		if(enabledUser)
			dateFrom = ExpandoUtil.getDateFrom(companyId, userId);

		if(enabledByCompany){
			if(companyEnableDate){
				if(dateFrom==null ){
					dateFrom = companyDateFrom;
				}
			}
		}
		
		if(enabledOverride)
			dateFrom = companyDateFrom;
		
		return dateFrom;
	}
	
	public static Date getDateTo(long companyId, long userId) {
		
		boolean enabledUser = ExpandoUtil.isAccessByDateEnabled(companyId, userId);
		boolean companyEnableDate = PreferencesUtil.isAccessByDateEnabled(companyId);
		boolean enabledByCompany = PreferencesUtil.isEnabledByCompany(companyId);
		boolean enabledOverride = PreferencesUtil.isEnabledOverrideUserSettings(companyId);
		
		Date dateTo = null; 
		Date companyDateTo = PreferencesUtil.getAccesDateTo(companyId);
		
		if(enabledUser)
			dateTo = ExpandoUtil.getDateTo(companyId, userId);

		if(enabledByCompany){

			if(companyEnableDate){
				if(dateTo==null){
					dateTo = companyDateTo;
				}
			}
		}
		
		if(enabledOverride)
			dateTo = companyDateTo;
		
		return dateTo;
	}

	public static void checkIPRange(long companyId, long usrId) throws AuthException  {

		List<String[]> allowedUserIPs = ExtendedAuthSettingsUtil.getAllowedUserIP(companyId, usrId);
		
		if(allowedUserIPs.size()>0){
			String ip = AuditRequestThreadLocal.getAuditThreadLocal().getClientIP();
			
			_log.debug("User IP is: " + ip);

			boolean contains = false;
			
			for (Iterator<String[]> iterator = allowedUserIPs.iterator(); (iterator.hasNext() && !contains);) {
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
			
			if(!contains){
				_log.info("User[" + usrId + "] can't login because his IP address[" + ip
						+ "] is not allowed in settings.");
				
				throw new NotAllowedIPAddressException();
			}
				
		}
	}
	
	
	private static final Log _log = LogFactoryUtil
			.getLog(ExtendedAuthSettingsUtil.class);
}
