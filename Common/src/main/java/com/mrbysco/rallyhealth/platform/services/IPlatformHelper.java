package com.mrbysco.rallyhealth.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public interface IPlatformHelper {

	/**
	 * Get the entity location from the registry
	 *
	 * @return The entity's resource location
	 */
	ResourceLocation getEntityLocation(EntityType<?> entityType);

	/**
	 * The amount of time before the risk disables itself
	 *
	 * @return The configured `riskTimer`
	 */
	int getRiskTimer();

	/**
	 * The chance of regaining the damage after striking back
	 *
	 * @return The configured `regainChance`
	 */
	double getRegainChance();

	/**
	 * The percentage of damage regained after striking back
	 *
	 * @return The configured `regainPercentage`
	 */
	double getRegainPercentage();
}
