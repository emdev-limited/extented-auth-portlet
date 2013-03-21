package ru.emdev.security.auth.util;

import java.util.Date;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

/**
 * @author Alexey Melnikov
 */
public class ExpandoUtil {

	private static final Log _log = LogFactoryUtil.getLog(ExpandoUtil.class);

	public static void createUserAttributes(long companyId) throws PortalException, SystemException {

		ExpandoTable userExandoTable = null;
		long tableId = 0l;

		try {
			userExandoTable = ExpandoTableLocalServiceUtil.getTable(companyId,
					User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
		} catch (Exception ex) {
		}

		if (userExandoTable == null) {
			userExandoTable = ExpandoTableLocalServiceUtil.addTable(companyId,
					User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME);
		}

		tableId = userExandoTable.getTableId();

		ExpandoColumnLocalServiceUtil.addColumn(tableId, ExpandoTableConstants.COLUMN_ALLOWED_IPS,
				ExpandoColumnConstants.STRING_ARRAY);

		ExpandoColumnLocalServiceUtil.addColumn(tableId,
				ExpandoTableConstants.COLUMN_ACCESS_BY_DATE, ExpandoColumnConstants.BOOLEAN);

		ExpandoColumnLocalServiceUtil.addColumn(tableId,
				ExpandoTableConstants.COLUMN_ACCESS_DATE_FROM, ExpandoColumnConstants.DATE);

		ExpandoColumnLocalServiceUtil.addColumn(tableId,
				ExpandoTableConstants.COLUMN_ACCESS_DATE_TO, ExpandoColumnConstants.DATE);

	}

	public static boolean isAccessByDateEnabled(long companyId, long userId) {
		boolean result = false;
		try {
			result = ExpandoValueLocalServiceUtil.getData(companyId, User.class.getName(),
					ExpandoTableConstants.DEFAULT_TABLE_NAME,
					ExpandoTableConstants.COLUMN_ACCESS_BY_DATE, userId, false);
		} catch (Exception e) {
			_log.error("Can't access to user[" + userId + "] attributes", e);
		}

		return result;
	}

	public static Date getDateFrom(long companyId, long userId) {
		Date result = null;
		try {
			result = ExpandoValueLocalServiceUtil.getData(companyId, User.class.getName(),
					ExpandoTableConstants.DEFAULT_TABLE_NAME,
					ExpandoTableConstants.COLUMN_ACCESS_DATE_FROM, userId, result);
		} catch (Exception e) {
			_log.error("Can't access to user[" + userId + "] attributes", e);
		}

		return result;
	}

	public static Date getDateTo(long companyId, long userId) {
		Date result = null;
		try {
			result = ExpandoValueLocalServiceUtil.getData(companyId, User.class.getName(),
					ExpandoTableConstants.DEFAULT_TABLE_NAME,
					ExpandoTableConstants.COLUMN_ACCESS_DATE_TO, userId, result);
		} catch (Exception e) {
			_log.error("Can't access to user[" + userId + "] attributes", e);
		}

		return result;
	}

	public static String[] getAllowedUserIP(long companyId, long userId) {
		String[] dataArray = null;
		try {
			dataArray = StringUtil.split(ExpandoValueLocalServiceUtil.getData(companyId,
					User.class.getName(), ExpandoTableConstants.DEFAULT_TABLE_NAME,
					ExpandoTableConstants.COLUMN_ACCESS_DATE_TO, userId, StringPool.BLANK),
					CharPool.NEW_LINE);
		} catch (Exception e) {
			_log.error("Can't access to user[" + userId + "] attributes", e);
		}

		return dataArray;
	}
}