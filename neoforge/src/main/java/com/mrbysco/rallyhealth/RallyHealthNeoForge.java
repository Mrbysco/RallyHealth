package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfigNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

@Mod(Constants.MOD_ID)
public class RallyHealthNeoForge {

	public RallyHealthNeoForge(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, RallyConfigNeoForge.commonSpec);
		eventBus.register(RallyConfigNeoForge.class);

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