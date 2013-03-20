package ru.emdev.auth.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.emdev.auth.SessionCountUtil;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;

public class PostLogoutAction extends Action {

	private static final Log _log = LogFactoryUtil.getLog(PostLogoutAction.class);

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ActionException {

		_log.debug("Start post logout action");

		long userId = PortalUtil.getUserId(request);
		SessionCountUtil.remove(userId, request.getSession().getId());

		_log.debug("End post logout action");
	}

}
