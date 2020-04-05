package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfig;
import com.mrbysco.rallyhealth.handlers.RallyHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class RallyHealth {
	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

	public RallyHealth() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RallyConfig.commonSpec);

		MinecraftForge.EVENT_BUS.register(new RallyHandler());
	}
}
