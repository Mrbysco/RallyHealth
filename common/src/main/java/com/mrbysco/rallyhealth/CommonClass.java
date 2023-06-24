package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class CommonClass {

	public static void onLivingHurt(LivingEntity livingEntity, DamageSource source, float amount) {
		Level level = livingEntity.level();
		if (!level.isClientSide && livingEntity instanceof Player player) {
			RallyData data = RallyData.get(level);
			Entity trueSource = source.getEntity();

			if (trueSource != null) {
				ResourceLocation mobLoc = Services.PLATFORM.getEntityLocation(trueSource.getType());

				float damageAmount = amount;
				if (damageAmount <= 0) return;

				if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
					damageAmount = CombatRules.getDamageAfterAbsorb(damageAmount, (float) player.getArmorValue(),
							(float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
				}
				int k = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), source);
				if (k > 0) {
					damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, (float) k);
				}

				data.putInfo(player.getUUID(), new RallyInfo(level.getGameTime(), damageAmount, mobLoc));
				data.setDirty(true);
			}
		}
	}

	public static void onLivingAttack(LivingEntity livingEntity, DamageSource source) {
		Level level = livingEntity.level();
		if (!level.isClientSide && source.getMsgId().equals("player")) {
			if (source.getEntity() instanceof Player player) {
				RallyData data = RallyData.get(level);
				RallyInfo info = data.getInfo(player.getUUID());
				if (info == null) return;

				ResourceLocation entityLocation = Services.PLATFORM.getEntityLocation(livingEntity.getType());
				ResourceLocation lastMob = info.mob();
				boolean withinTime = data.isWithinRiskTimer(player.getUUID(), level.getGameTime());
				if (entityLocation != null && entityLocation.equals(lastMob)) {
					if (withinTime) {
						if (level.random.nextDouble() <= Services.PLATFORM.getRegainChance()) {
							float heal = info.damage();
							int actuallyGained = Math.max(1, (int) (heal * Services.PLATFORM.getRegainPercentage()));
							player.heal(actuallyGained);
							data.removeInfo(player.getUUID());
							data.setDirty(true);
						}
					} else {
						data.removeInfo(player.getUUID());
						data.setDirty();
					}
				}
			}
		}
	}
}