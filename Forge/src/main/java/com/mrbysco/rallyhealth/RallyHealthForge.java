package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class RallyHealthForge {

	public RallyHealthForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RallyConfigForge.commonSpec);
		eventBus.register(RallyConfigForge.class);

		MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
		MinecraftForge.EVENT_BUS.addListener(this::onLivingAttack);
	}

	private void onLivingHurt(LivingHurtEvent event) {
		CommonClass.onLivingHurt(event.getEntity(), event.getSource(), event.getAmount());
	}

	private void onLivingAttack(LivingAttackEvent event) {
		CommonClass.onLivingAttack(event.getEntity(), event.getSource());
	}
}