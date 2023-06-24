package com.mrbysco.rallyhealth.platform;

import com.mrbysco.rallyhealth.config.RallyConfigForge;
import com.mrbysco.rallyhealth.platform.services.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public ResourceLocation getEntityLocation(EntityType<?> entityType) {
		return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
	}

	@Override
	public int getRiskTimer() {
		return RallyConfigForge.COMMON.riskTimer.get();
	}

	@Override
	public double getRegainChance() {
		return RallyConfigForge.COMMON.regainChance.get();
	}

	@Override
	public double getRegainPercentage() {
		return RallyConfigForge.COMMON.regainPercentage.get();
	}
}
