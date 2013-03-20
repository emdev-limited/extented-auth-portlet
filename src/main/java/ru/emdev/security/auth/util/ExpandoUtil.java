package ru.emdev.security.auth.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

/**
 * @author Alexey Melnikov
 */
public class ExpandoUtil {

	public static void createUserAttributes(long companyId) throws PortalException, SystemException {
		// create expando table for user
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
}
