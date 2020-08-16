package com.mrbysco.rallyhealth.config;

import com.mrbysco.rallyhealth.RallyHealth;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class RallyConfig {
	public static class Common {
		public final DoubleValue riskTimer;
		public final IntValue regainChance;
		public final DoubleValue regainPercentage;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Rally settings")
					.push("Common");

			riskTimer = builder
					.comment("The amount of time before the risk disables itself (Default: 3.5)")
					.defineInRange("riskTimer", 3.5D, 0.05D, Double.MAX_VALUE);

			regainChance = builder
					.comment("The chance of regaining the damage after striking back [Out of 10] (Default 7)")
					.defineInRange("regainChance", 7, 1, 10);

			regainPercentage = builder
					.comment("The percentage of damage regained after striking back (Default 1.0)")
					.defineInRange("regainPercentage", 1.0, 0.001, 1.0);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		RallyHealth.logger.debug("Loaded Rally Health's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		RallyHealth.logger.debug("Rally Health's config just got changed on the file system!");
	}
}
