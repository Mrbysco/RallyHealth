package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.callback.LivingCallback;
import com.mrbysco.rallyhealth.config.RallyConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.InteractionResult;

public class RallyHealthFabric implements ModInitializer {
	public static ConfigHolder<RallyConfigFabric> config;

	@Override
	public void onInitialize() {
		config = AutoConfig.register(RallyConfigFabric.class, Toml4jConfigSerializer::new);

		LivingCallback.HURT_EVENT.register((living, source, amount) -> {
			CommonClass.onLivingHurt(living, source, amount);
			return InteractionResult.PASS;
		});

		LivingCallback.ATTACK_EVENT.register((living, source, amount) -> {
			CommonClass.onLivingAttack(living, source);
			return InteractionResult.PASS;
		});
	}
}
