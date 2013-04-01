/**
 * 
 */
package ru.emdev.security.auth.hook.listener;

import org.apache.commons.lang.ArrayUtils;

import ru.emdev.security.auth.util.ExpandoTableConstants;
import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;

/**
 * @author Alexey Melnikov
 */
public class CompanyListener extends BaseModelListener<Company> {

	private static Log _log = LogFactoryUtil.getLog(CompanyListener.class);

	@Override
	public void onAfterUpdate(Company company) throws ModelListenerException {
		long companyId = company.getCompanyId();
		if (!ArrayUtils.contains(PortalUtil.getCompanyIds(), companyId)) {
			try {
				if (_log.isInfoEnabled())
					_log.info("Creating user attributes for extended authenticator in company["
							+ companyId + "]");

				long tableId = ExpandoUtil.getExpandoTable(companyId, User.class.getName());

				ExpandoColumnLocalServiceUtil.addColumn(tableId,
						ExpandoTableConstants.COLUMN_ALLOWED_IPS, ExpandoColumnConstants.STRING_ARRAY);

				ExpandoColumnLocalServiceUtil.addColumn(tableId,
						ExpandoTableConstants.COLUMN_ACCESS_BY_DATE, ExpandoColumnConstants.BOOLEAN);

				ExpandoColumnLocalServiceUtil.addColumn(tableId,
						ExpandoTableConstants.COLUMN_ACCESS_DATE_FROM, ExpandoColumnConstants.DATE);

				ExpandoColumnLocalServiceUtil.addColumn(tableId,
						ExpandoTableConstants.COLUMN_ACCESS_DATE_TO, ExpandoColumnConstants.DATE);

				ExpandoColumnLocalServiceUtil.addColumn(tableId,
						ExpandoTableConstants.COLUMN_SESSION_COUNT, ExpandoColumnConstants.INTEGER);

			} catch (Exception e) {
				_log.error("Failed to create user attributes for company[" + companyId + "]", e);
			}
		}
	}
}
