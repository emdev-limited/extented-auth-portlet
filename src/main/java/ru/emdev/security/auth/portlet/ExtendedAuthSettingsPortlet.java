package ru.emdev.security.auth.portlet;

import java.util.Calendar;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ProcessAction;

import ru.emdev.security.auth.util.PropsKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Jose Campos
 *
 */
public class ExtendedAuthSettingsPortlet extends MVCPortlet {
 
	@ProcessAction(name="saveSettings")
	public void saveSettings (ActionRequest actionRequest, 
			ActionResponse actionResponse) throws Exception{
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		int startDateMonth = ParamUtil.getInteger(
			actionRequest, "startDateMonth");
		int startDateDay = ParamUtil.getInteger(
			actionRequest, "startDateDay");
		int startDateYear = ParamUtil.getInteger(
			actionRequest, "startDateYear");
		int startDateHour = ParamUtil.getInteger(
			actionRequest, "startDateHour");
		int startDateMinute = ParamUtil.getInteger(
			actionRequest, "startDateMinute");
		int startDateAmPm = ParamUtil.getInteger(
			actionRequest, "startDateAmPm");

		if (startDateAmPm == Calendar.PM) {
			startDateHour += 12;
		}

		Date startDate = PortalUtil.getDate(
			startDateMonth, startDateDay, startDateYear, startDateHour,
			startDateMinute, themeDisplay.getTimeZone(),
			PortalException.class);

		int endDateMonth = ParamUtil.getInteger(
			actionRequest, "endDateMonth");
		int endDateDay = ParamUtil.getInteger(
			actionRequest, "endDateDay");
		int endDateYear = ParamUtil.getInteger(
			actionRequest, "endDateYear");
		int endDateHour = ParamUtil.getInteger(
			actionRequest, "endDateHour");
		int endDateMinute = ParamUtil.getInteger(
			actionRequest, "endDateMinute");
		int endDateAmPm = ParamUtil.getInteger(
			actionRequest, "endDateAmPm");

		if (endDateAmPm == Calendar.PM) {
			endDateHour += 12;
		}

		Date endDate = PortalUtil.getDate(
			endDateMonth, endDateDay, endDateYear, endDateHour,
			endDateMinute, themeDisplay.getTimeZone(),
			PortalException.class);
		
		UnicodeProperties properties = PropertiesParamUtil.getProperties(
				actionRequest, "settings--");
		
		properties.put(PropsKeys.EXTENDED_AUTH_ACCESS_DATE_FROM, String.valueOf(startDate.getTime()));
		properties.put(PropsKeys.EXTENDED_AUTH_ACCESS_DATE_TO, String.valueOf(endDate.getTime()));
		
		
		CompanyLocalServiceUtil.updatePreferences(PortalUtil.getCompanyId(actionRequest), properties);
		
		_log.debug(properties.toString());
		
	}

	private static Log _log = LogFactoryUtil.getLog(ExtendedAuthSettingsPortlet.class);
}
