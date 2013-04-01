package ru.emdev.security.auth.hook.upgrade.v6_1_1_2;

import java.util.List;

import ru.emdev.security.auth.hook.upgrade.v6_1_1_1.OldExpandoColumns;
import ru.emdev.security.auth.util.ExpandoTableConstants;
import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
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

			ExpandoColumnLocalServiceUtil.addColumn(tableId,
					ExpandoTableConstants.COLUMN_SESSION_COUNT, ExpandoColumnConstants.INTEGER);

			// upgrade old columns
			ExpandoColumn column = null;
			column = ExpandoColumnLocalServiceUtil
						.getColumn(tableId, OldExpandoColumns.COLUMN_ALLOWED_IPS);
			if (column != null) {
				column.setName(ExpandoTableConstants.COLUMN_ALLOWED_IPS);
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);
			}

			column = ExpandoColumnLocalServiceUtil
						.getColumn(tableId, OldExpandoColumns.COLUMN_ACCESS_BY_DATE);
			if (column != null) {
				column.setName(ExpandoTableConstants.COLUMN_ACCESS_BY_DATE);
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);
			}

			column = ExpandoColumnLocalServiceUtil
						.getColumn(tableId, OldExpandoColumns.COLUMN_ACCESS_DATE_FROM);
			if (column != null) {
				column.setName(ExpandoTableConstants.COLUMN_ACCESS_DATE_FROM);
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);
			}

			column = ExpandoColumnLocalServiceUtil
						.getColumn(tableId, OldExpandoColumns.COLUMN_ACCESS_DATE_TO);
			if (column != null) {
				column.setName(ExpandoTableConstants.COLUMN_ACCESS_DATE_TO);
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);
			}
		}
	}
}
