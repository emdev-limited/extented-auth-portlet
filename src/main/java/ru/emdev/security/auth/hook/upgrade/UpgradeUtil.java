package ru.emdev.security.auth.hook.upgrade;

import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;

import java.util.List;

/**
 * @author Ryan Park
 */
public class UpgradeUtil {

	protected static boolean isFirstRun() throws Exception {
		List<Company> companies = CompanyLocalServiceUtil.getCompanies();

		if (companies.isEmpty()) {
			return false;
		}

		return true;
	}

}