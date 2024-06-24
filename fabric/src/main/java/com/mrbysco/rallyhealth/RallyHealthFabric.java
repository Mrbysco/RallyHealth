package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.callback.LivingCallback;
import com.mrbysco.rallyhealth.config.RallyConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class RallyHealthFabric implements ModInitializer {
	public static ConfigHolder<RallyConfigFabric> config;

	@Override
	public void onInitialize() {
		config = AutoConfig.register(RallyConfigFabric.class, Toml4jConfigSerializer::new);

		LivingCallback.HURT_EVENT.register((living, source, amount) -> {
			Level level = living.level();
			if (level instanceof ServerLevel serverLevel && living instanceof Player player) {
				float damageAmount = amount;
				if (damageAmount <= 0) return InteractionResult.PASS;

				if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
					damageAmount = CombatRules.getDamageAfterAbsorb(player, damageAmount, source, (float) player.getArmorValue(),
							(float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
				}
				float k = EnchantmentHelper.getDamageProtection(serverLevel, player, source);
				if (k > 0) {
					damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, (float) k);
				}

				CommonClass.onLivingHurt(living, source, damageAmount);
			}

			return InteractionResult.PASS;
		});

		LivingCallback.ATTACK_EVENT.register((living, source, amount) -> {
			CommonClass.onLivingAttack(living, source);
			return InteractionResult.PASS;
		});
	}
}
