package com.mrbysco.rallyhealth.config;

import com.mrbysco.rallyhealth.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Constants.MOD_ID)
public class RallyConfigFabric implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public General general = new General();

	public static class General {

		@Comment("The amount of time before the risk disables itself (Default: 70)")
		@ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
		public int riskTimer = 70;

		@Comment("The chance of regaining the damage after striking back [0.7 = 70%] (Default 0.7)")
		@ConfigEntry.BoundedDiscrete(min = 0, max = 1)
		public double regainChance = 0.7D;

		@Comment("The percentage of damage regained after striking back (1.0 = 100%) (Default 1.0)")
		@ConfigEntry.BoundedDiscrete(min = 0, max = 1)
		public double regainPercentage = 1.0;
	}
}