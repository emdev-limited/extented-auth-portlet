package ru.emdev.security.auth.hook.upgrade;

import ru.emdev.security.auth.hook.upgrade.v6_1_1_2.UpgradeUser;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Alexey Melnikov
 */
public class UpgradeProcess_6_1_1_2 extends UpgradeProcess {

	@Override
	public int getThreshold() {
		return 6112;
	}

	@Override
	protected void doUpgrade() throws Exception {
		upgrade(UpgradeUser.class);
	}
}
