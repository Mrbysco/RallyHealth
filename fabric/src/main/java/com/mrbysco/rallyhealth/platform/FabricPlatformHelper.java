package com.mrbysco.rallyhealth.platform;

import com.mrbysco.rallyhealth.RallyHealthFabric;
import com.mrbysco.rallyhealth.platform.services.IPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public ResourceLocation getEntityLocation(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

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
