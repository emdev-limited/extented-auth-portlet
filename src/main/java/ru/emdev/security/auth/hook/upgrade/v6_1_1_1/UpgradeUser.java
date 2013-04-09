package ru.emdev.security.auth.hook.upgrade.v6_1_1_1;

import java.util.List;

import ru.emdev.security.auth.hook.listener.CompanyListener;
import ru.emdev.security.auth.util.ExpandoTableConstants;
import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;

/**
 * @author Alexey Melnikov
 */
public class UpgradeUser extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		List<Company> companies = CompanyLocalServiceUtil.getCompanies();

		for (Company company : companies) {
			long companyId = company.getCompanyId();

			try {
				if (CompanyListener._log.isInfoEnabled())
					CompanyListener._log.info("Creating user attributes for extended authenticator in company["
							+ companyId + "]");

				ExpandoUtil.addUserExpandoColumn(companyId,
					ExpandoTableConstants.COLUMN_ALLOWED_IPS, ExpandoColumnConstants.STRING_ARRAY);

				ExpandoUtil.addUserExpandoColumn(companyId,
					ExpandoTableConstants.COLUMN_ACCESS_BY_DATE, ExpandoColumnConstants.BOOLEAN);

				ExpandoUtil.addUserExpandoColumn(companyId,
					ExpandoTableConstants.COLUMN_ACCESS_DATE_FROM, ExpandoColumnConstants.DATE);

				ExpandoUtil.addUserExpandoColumn(companyId,
					ExpandoTableConstants.COLUMN_ACCESS_DATE_TO, ExpandoColumnConstants.DATE);

				ExpandoUtil.addUserExpandoColumn(companyId,
					ExpandoTableConstants.COLUMN_SESSION_COUNT, ExpandoColumnConstants.INTEGER);		
		
			} catch (Exception e) {
				CompanyListener._log.error("Failed to create user attributes for company[" + companyId + "]", e);
			}
		}
	}
}
