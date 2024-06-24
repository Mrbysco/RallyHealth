package com.mrbysco.rallyhealth.platform;

import com.mrbysco.rallyhealth.RallyHealthFabric;
import com.mrbysco.rallyhealth.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public int getRiskTimer() {
		return RallyHealthFabric.config.get().general.riskTimer;
	}

	@Override
	public double getRegainChance() {
		return RallyHealthFabric.config.get().general.regainChance;
	}

	@Override
	public double getRegainPercentage() {
		return RallyHealthFabric.config.get().general.regainPercentage;
	}
}
