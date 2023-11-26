package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfigForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

@Mod(Constants.MOD_ID)
public class RallyHealthForge {

	public RallyHealthForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RallyConfigForge.commonSpec);
		eventBus.register(RallyConfigForge.class);

		NeoForge.EVENT_BUS.addListener(this::onLivingHurt);
		NeoForge.EVENT_BUS.addListener(this::onLivingAttack);
	}

	private void onLivingHurt(LivingHurtEvent event) {
		CommonClass.onLivingHurt(event.getEntity(), event.getSource(), event.getAmount());
	}

	private void onLivingAttack(LivingAttackEvent event) {
		CommonClass.onLivingAttack(event.getEntity(), event.getSource());
	}
}