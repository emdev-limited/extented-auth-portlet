/**
 * 
 */
package ru.emdev.security.auth.hook.listener;

import org.apache.commons.lang.ArrayUtils;

import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Company;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Alexey Melnikov
 */
public class CompanyListener extends BaseModelListener<Company> {

	private static Log _log = LogFactoryUtil.getLog(CompanyListener.class);

	@Override
	public void onAfterUpdate(Company company) throws ModelListenerException {
		if (!ArrayUtils.contains(PortalUtil.getCompanyIds(), company.getCompanyId())) {
			try {
				if (_log.isDebugEnabled())
					_log.debug("Creating user attributes for extended authenticator in company["
							+ company.getCompanyId() + "]");

				ExpandoUtil.createUserAttributes(company.getCompanyId());

			} catch (Exception e) {
				_log.error("Failed to create user attributes for company[" + company.getCompanyId()
						+ "]", e);
			}
		}
	}
}
