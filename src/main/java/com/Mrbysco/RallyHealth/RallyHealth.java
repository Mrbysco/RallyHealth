package com.mrbysco.rallyhealth;

import com.mojang.logging.LogUtils;
import com.mrbysco.rallyhealth.config.RallyConfig;
import com.mrbysco.rallyhealth.handlers.RallyHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class RallyHealth {
	public static final Logger LOGGER = LogUtils.getLogger();

	public RallyHealth() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RallyConfig.commonSpec);
		eventBus.register(RallyConfig.class);

		MinecraftForge.EVENT_BUS.register(new RallyHandler());
	}
}
