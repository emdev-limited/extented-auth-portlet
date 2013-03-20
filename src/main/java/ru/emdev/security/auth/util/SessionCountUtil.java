package ru.emdev.security.auth.util;

import java.util.HashSet;
import java.util.Set;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class SessionCountUtil {

	private static final Log _log = LogFactoryUtil.getLog(SessionCountUtil.class);

	public static final String AUTH_CACHE_NAME = "customAuthCache";

	public static final String KEY_SESSIONS_CACHE = "sessionsCache";

	private static final PortalCache _portalCache = MultiVMPoolUtil.getCache(AUTH_CACHE_NAME);

	public static void add(long userId, String sessionId) {
		Set<String> sessions = getSessions(userId);
		if (sessions == null) {
			if (_log.isDebugEnabled())
				_log.debug("Creating session cache for user[" + userId + "]");
			sessions = new HashSet<String>(3);
			_portalCache.put(KEY_SESSIONS_CACHE + userId, sessions);
		}
		sessions.add(sessionId);
		if (_log.isDebugEnabled())
			_log.debug("Added session[" + sessionId + "] to cache for user[" + userId + "]");
	}

	public static boolean remove(long userId, String sessionId) {
		if (_log.isDebugEnabled())
			_log.debug("Removing session[" + sessionId + "] from cache for user[" + userId + "]");

		Set<String> sessions = getSessions(userId);

		boolean removed = sessions != null && sessions.remove(sessionId);
		if (_log.isDebugEnabled() && removed)
			_log.debug("Removed session[" + sessionId + "] from cache for user[" + userId + "]");

		return removed;
	}

	public static int count(long userId) {
		Set<String> sessions = getSessions(userId);
		int count = sessions == null ? 0 : sessions.size();
		if (_log.isDebugEnabled())
			_log.debug("Session count is " + count + " for user[" + userId + "]");
		return count;
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getSessions(long userId) {
		return (Set<String>) _portalCache.get(KEY_SESSIONS_CACHE + userId);
	}

}