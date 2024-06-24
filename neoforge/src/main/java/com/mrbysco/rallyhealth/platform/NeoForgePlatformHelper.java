package com.mrbysco.rallyhealth.platform;

import com.mrbysco.rallyhealth.config.RallyConfigNeoForge;
import com.mrbysco.rallyhealth.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public int getRiskTimer() {
		return RallyConfigNeoForge.COMMON.riskTimer.get();
	}

	@Override
	public double getRegainChance() {
		return RallyConfigNeoForge.COMMON.regainChance.get();
	}

	@Override
	public double getRegainPercentage() {
		return RallyConfigNeoForge.COMMON.regainPercentage.get();
	}
}
