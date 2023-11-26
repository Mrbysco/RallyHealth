package com.mrbysco.rallyhealth.config;

import com.mrbysco.rallyhealth.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import org.apache.commons.lang3.tuple.Pair;

public class RallyConfigForge {
	public static class Common {
		public final ModConfigSpec.IntValue riskTimer;
		public final DoubleValue regainChance;
		public final DoubleValue regainPercentage;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("Rally settings")
					.push("Common");

			riskTimer = builder
					.comment("The amount of time before the risk disables itself (Default: 70)")
					.defineInRange("riskTimer", 70, 1, Integer.MAX_VALUE);

			regainChance = builder
					.comment("The chance of regaining the damage after striking back [0.7 = 70%] (Default 0.7)")
					.defineInRange("regainChance", 0.7D, 0, 1);

			regainPercentage = builder
					.comment("The percentage of damage regained after striking back (1.0 = 100%) (Default 1.0)")
					.defineInRange("regainPercentage", 1.0, 0.001, 1.0);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Constants.LOGGER.debug("Loaded Rally Health's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Constants.LOGGER.debug("Rally Health's config just got changed on the file system!");
	}
}
