package ru.emdev.security.auth.hook.upgrade.v6_1_1_1;

import java.util.List;

import ru.emdev.security.auth.util.ExpandoUtil;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;

/**
 * @author Alexey Melnikov
 */
public class UpgradeUser extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {

		List<Company> companies = CompanyLocalServiceUtil.getCompanies();
		
		for (Company company : companies)
			ExpandoUtil.createUserAttributes(company.getCompanyId());
	}
}
