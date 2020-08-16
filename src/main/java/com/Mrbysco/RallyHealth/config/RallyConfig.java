package com.mrbysco.rallyhealth.config;

import com.mrbysco.rallyhealth.RallyHealth;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class RallyConfig {
	public static class Common {
		public final DoubleValue riskTimer;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Rally settings")
					.push("Common");

			riskTimer = builder
					.comment("The amount of time before the risk disables itself (Default: 3.5)")
					.defineInRange("riskTimer", 3.5D, 0.05D, Double.MAX_VALUE);

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
