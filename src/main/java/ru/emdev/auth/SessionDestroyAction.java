package ru.emdev.auth;

import javax.servlet.http.HttpSession;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;

public class SessionDestroyAction extends SessionAction {

	private static final Log _log = LogFactoryUtil.getLog(SessionDestroyAction.class);

	@Override
	public void run(HttpSession session) throws ActionException {
		_log.debug("Start custom session destroy action");

		User user = (User) session.getAttribute(WebKeys.USER);
		long userId = 0l;
		if (user == null) {
			String jRemoteUser = null;

			if (GetterUtil.getBoolean(PropsUtil.get(PropsKeys.PORTAL_JAAS_ENABLE))) {
				jRemoteUser = (String) session.getAttribute("j_remoteuser");
			}

			if (Validator.isNotNull(jRemoteUser)) {
				userId = GetterUtil.getLong(jRemoteUser);
			} else {
				userId = (Long) session.getAttribute(WebKeys.USER_ID);
			}
		} else {
			userId = user.getUserId();
		}

		if (userId > 0) {
			SessionCountUtil.remove(userId, session.getId());
		}

		_log.debug("End custom session destroy action");
	}

}
