package ru.emdev.security.auth.hook.upgrade.v6_1_1_1;

import java.util.List;

import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;

/**
 * @author Alexey Melnikov
 */
public class UpgradeUser extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		List<Company> companies = CompanyLocalServiceUtil.getCompanies();

		for (Company company : companies) {
			long companyId = company.getCompanyId();
			long tableId = ExpandoUtil.getExpandoTable(companyId, User.class.getName());

			ExpandoColumnLocalServiceUtil.addColumn(tableId, OldExpandoColumns.COLUMN_ALLOWED_IPS,
					ExpandoColumnConstants.STRING_ARRAY);

			ExpandoColumnLocalServiceUtil.addColumn(tableId,
					OldExpandoColumns.COLUMN_ACCESS_BY_DATE, ExpandoColumnConstants.BOOLEAN);

			ExpandoColumnLocalServiceUtil.addColumn(tableId,
					OldExpandoColumns.COLUMN_ACCESS_DATE_FROM, ExpandoColumnConstants.DATE);

			ExpandoColumnLocalServiceUtil.addColumn(tableId,
					OldExpandoColumns.COLUMN_ACCESS_DATE_TO, ExpandoColumnConstants.DATE);

		}
	}
}
