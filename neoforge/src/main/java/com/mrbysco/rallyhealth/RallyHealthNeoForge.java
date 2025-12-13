package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfigNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@Mod(Constants.MOD_ID)
public class RallyHealthNeoForge {

	public RallyHealthNeoForge(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, RallyConfigNeoForge.commonSpec);
		eventBus.register(RallyConfigNeoForge.class);

		NeoForge.EVENT_BUS.addListener(this::onLivingHurt);
		NeoForge.EVENT_BUS.addListener(this::onLivingAttack);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	private void onLivingHurt(LivingDamageEvent.Post event) {
		CommonClass.onLivingHurt(event.getEntity(), event.getSource(), event.getNewDamage());
	}

	private void onLivingAttack(LivingIncomingDamageEvent event) {
		CommonClass.onLivingAttack(event.getEntity(), event.getSource());
	}
}