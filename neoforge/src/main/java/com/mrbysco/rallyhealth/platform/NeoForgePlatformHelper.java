package com.mrbysco.rallyhealth.platform;

import com.mrbysco.rallyhealth.config.RallyConfigNeoForge;
import com.mrbysco.rallyhealth.platform.services.IPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public ResourceLocation getEntityLocation(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

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
