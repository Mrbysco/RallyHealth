package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.config.RallyConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CommonClass {

	public static void onLivingHurt(LivingEntity livingEntity, DamageSource source, float lostAmount) {
		Level level = livingEntity.level();
		if (level instanceof ServerLevel && livingEntity instanceof Player player) {
			RallyData data = RallyData.get(level);
			Entity trueSource = source.getEntity();

			if (trueSource != null) {
				Identifier mobLoc = BuiltInRegistries.ENTITY_TYPE.getKey(trueSource.getType());

				data.putInfo(player.getUUID(), new RallyInfo(level.getGameTime(), lostAmount, mobLoc));
				data.setDirty(true);
			}
		}
	}

	public static void onLivingAttack(LivingEntity livingEntity, DamageSource source) {
		Level level = livingEntity.level();
		if (!level.isClientSide() && source.getMsgId().equals("player")) {
			if (source.getEntity() instanceof Player player) {
				RallyData data = RallyData.get(level);
				RallyInfo info = data.getInfo(player.getUUID());
				if (info == null) return;

				Identifier entityLocation = BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType());
				Identifier lastMob = info.mob();
				boolean withinTime = data.isWithinRiskTimer(player.getUUID(), level.getGameTime());
				if (entityLocation != null && entityLocation.equals(lastMob)) {
					if (withinTime) {
						if (level.getRandom().nextDouble() <= RallyConfig.COMMON.regainChance.get()) {
							float heal = info.damage();
							int actuallyGained = Math.max(1, (int) (heal * RallyConfig.COMMON.regainPercentage.get()));
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