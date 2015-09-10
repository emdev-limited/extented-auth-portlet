<%@include file="../init.jsp" %>

<%
	boolean extAuthEnabledAccessAdmin = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ENABLED_ACCESS_ADMIN);
	boolean extAuthEnabledByCompany = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ENABLED_BY_COMPANY);
	boolean extAuthOverrideUserSettings = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ENABLED_OVERRIDE_USER_SETTINGS);
	boolean extAuthRangeDate = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ENABLED_DATE_RANGE);
	long accessDateFrom = PrefsPropsUtil.getLong(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ACCESS_DATE_FROM);
	long accessDateTo = PrefsPropsUtil.getLong(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_ACCESS_DATE_TO);
	String ipRange = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_IP_RANGE);
	String emailDomains = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.EXTENDED_AUTH_EMAIL_DOMAINS);
	int maxSessionCount = PrefsPropsUtil.getInteger(company.getCompanyId(), PropsKeys.MAX_SESSION_COUNT_FOR_USER);
	
	Calendar startDate = CalendarFactoryUtil.getCalendar(timeZone, locale);
	Calendar endDate = CalendarFactoryUtil.getCalendar(timeZone, locale);

	if(accessDateFrom>0){
		startDate.setTimeInMillis(accessDateFrom);
	}
	
	if(accessDateTo>0){
		endDate.setTimeInMillis(accessDateTo);
	}else{
		endDate.add(Calendar.DAY_OF_MONTH, 1);
	}
	
%>
<portlet:actionURL name="saveSettings" var="actionURL" />

<aui:form action="<%= actionURL %>" method="post" name="fm" >

<aui:input name="companyId" type="hidden" value="<%=company.getCompanyId() %>"></aui:input>

<liferay-ui:tabs names="general,date,ip-ranges,session" refresh="false">
	<liferay-ui:section  >
		<aui:fieldset >
			<aui:input label="enabled-extended-authentication-by-company" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_ENABLED_BY_COMPANY + "--" %>' 
							type="checkbox"  helpMessage="enabled-extended-authentication-by-company-help" checked="<%=extAuthEnabledByCompany%>"/>
							
			<aui:input label="enabled-override-user-extended-authentication" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_ENABLED_OVERRIDE_USER_SETTINGS + "--"%>' 
							type="checkbox"  helpMessage="enabled-override-user-extended-authentication-help" checked="<%=extAuthOverrideUserSettings %>" />
			
			<aui:input label="enabled-access-admin" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_ENABLED_ACCESS_ADMIN + "--"%>' 
							type="checkbox"  helpMessage="enabled-access-admin-help" checked="<%=extAuthEnabledAccessAdmin %>" />
			
		</aui:fieldset>
	</liferay-ui:section>
	
	<liferay-ui:section>
		<aui:fieldset>
			<aui:input label="enabled-access-by-date" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_ENABLED_DATE_RANGE + "--" %>'
							type="checkbox" helpMessage="enabled-access-by-date-help" checked="<%=extAuthRangeDate %>"/>
		<aui:field-wrapper label="access-date-from">
			<liferay-ui:input-date
				dayParam="startDateDay"
				dayValue="<%= startDate.get(Calendar.DATE) %>"
				disabled="<%= false %>"
				firstDayOfWeek="<%= startDate.getFirstDayOfWeek() - 1 %>"
				monthParam="startDateMonth"
				monthValue="<%= startDate.get(Calendar.MONTH) %>"
				yearParam="startDateYear"
				yearValue="<%= startDate.get(Calendar.YEAR) %>"
			/>

			<liferay-ui:input-time
				amPmParam='<%= "startDateAmPm" %>'
				amPmValue="<%= startDate.get(Calendar.AM_PM) %>"
				disabled="<%= false %>"
				hourParam='<%= "startDateHour" %>'
				hourValue="<%= startDate.get(Calendar.HOUR) %>"
				minuteInterval="<%= 30 %>"
				minuteParam='<%= "startDateMinute" %>'
				minuteValue="<%= startDate.get(Calendar.MINUTE) %>"
			/>
		</aui:field-wrapper>
			
		<aui:field-wrapper label="access-date-to">
			<liferay-ui:input-date
				dayParam="endDateDay"
				dayValue="<%= endDate.get(Calendar.DATE) %>"
				disabled="<%= false %>"
				firstDayOfWeek="<%= endDate.getFirstDayOfWeek() - 1 %>"
				monthParam="endDateMonth"
				monthValue="<%= endDate.get(Calendar.MONTH) %>"
				yearParam="endDateYear"
				yearValue="<%= endDate.get(Calendar.YEAR) %>"
			/>

			<liferay-ui:input-time
				amPmParam='<%= "endDateAmPm" %>'
				amPmValue="<%= endDate.get(Calendar.AM_PM) %>"
				disabled="<%= false %>"
				hourParam='<%= "endDateHour" %>'
				hourValue="<%= endDate.get(Calendar.HOUR) %>"
				minuteInterval="<%= 30 %>"
				minuteParam='<%= "endDateMinute" %>'
				minuteValue="<%= endDate.get(Calendar.MINUTE) %>"
			/>
		</aui:field-wrapper>
															
			
		</aui:fieldset>
	</liferay-ui:section>
	<liferay-ui:section>
		<aui:fieldset>
			<aui:input label="allowed-ip-addresses" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_IP_RANGE + "--" %>' type="textarea" value="<%=ipRange %>" />
			<aui:input label="email-domains" name='<%= "settings--" + PropsKeys.EXTENDED_AUTH_EMAIL_DOMAINS + "--" %>'  type="textarea" value="<%=emailDomains %>" helpMessage="email-domains-help"/>
		</aui:fieldset>
	</liferay-ui:section>
	<liferay-ui:section>
		<aui:fieldset>
			<aui:input label="simultaneous-session-count" name='<%= "settings--" + PropsKeys.MAX_SESSION_COUNT_FOR_USER + "--" %>' value="<%=maxSessionCount %>"></aui:input>
		</aui:fieldset>
	</liferay-ui:section>
	
</liferay-ui:tabs>

<aui:button-row>
	<aui:button type="submit" ></aui:button>
</aui:button-row>
</aui:form>
