package com.mrbysco.rallyhealth.platform.services;

public interface IPlatformHelper {

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
