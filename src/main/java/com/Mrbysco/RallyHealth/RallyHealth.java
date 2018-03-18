package com.Mrbysco.RallyHealth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.RallyHealth.config.RallyConfigGen;
import com.Mrbysco.RallyHealth.handlers.RallyHandler;
import com.Mrbysco.RallyHealth.proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RallyReference.MOD_ID, 
	name = RallyReference.MOD_NAME, 
	version = RallyReference.VERSION, 
	acceptedMinecraftVersions = RallyReference.ACCEPTED_VERSIONS)

public class RallyHealth {
	@Instance(RallyReference.MOD_ID)
	public static RallyHealth instance;
	
	@SidedProxy(clientSide = RallyReference.CLIENT_PROXY_CLASS, serverSide = RallyReference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(RallyReference.MOD_ID);
		
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{	
		logger.debug("Registering Config");
		MinecraftForge.EVENT_BUS.register(new RallyConfigGen());
		
		proxy.Preinit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		logger.debug("Registering Handler");
		MinecraftForge.EVENT_BUS.register(new RallyHandler());
		
		proxy.Init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
