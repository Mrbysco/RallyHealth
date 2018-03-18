package com.Mrbysco.RallyHealth.config;

import com.Mrbysco.RallyHealth.RallyReference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RallyReference.MOD_ID)
@Config.LangKey("rallyhealth.config.title")
public class RallyConfigGen {
	@Config.Comment({"General Configuration"})
	public static General general = new General();
	
	public static class General{
		@Config.Comment("The amount of time before the risk disables itself (Default: 3.5)")
		public double riskTimer = 3.5;
	}
	
	@Mod.EventBusSubscriber(modid = RallyReference.MOD_ID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(RallyReference.MOD_ID)) {
				ConfigManager.sync(RallyReference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
