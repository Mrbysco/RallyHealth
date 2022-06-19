package com.mrbysco.rallyhealth.handlers;

import com.mrbysco.rallyhealth.Reference;
import com.mrbysco.rallyhealth.config.RallyConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

public class RallyHandler {

	@SubscribeEvent
	public void DamageHandler(LivingHurtEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		if (!livingEntity.level.isClientSide && livingEntity instanceof Player) {
			Player player = (Player) event.getEntityLiving();
			CompoundTag playerData = player.getPersistentData();
			DamageSource source = event.getSource();
			Entity trueSource = event.getSource().getEntity();

			if (trueSource != null) {
				ResourceLocation mobLoc = ForgeRegistries.ENTITIES.getKey(trueSource.getType());
				String damageMob = mobLoc != null ? mobLoc.toString() : "";
				float damageAmount = event.getAmount();
				if (damageAmount <= 0) return;

				if (!source.isBypassArmor()) {
					damageAmount = CombatRules.getDamageAfterAbsorb(damageAmount, (float) player.getArmorValue(), (float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
				}
				int k = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), source);
				if (k > 0) {
					damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, (float) k);
				}

				playerData.putFloat(Reference.LAST_DAMAGE_TAG, damageAmount);
				playerData.putBoolean(Reference.AT_RISK_TAG, true);
				playerData.putInt(Reference.RISK_TIME_TAG, 0);
				playerData.putString(Reference.LAST_MOB_TAG, damageMob);
			}
		}
	}

	@SubscribeEvent
	public void livingAttack(LivingAttackEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		if (!livingEntity.level.isClientSide && event.getSource().getMsgId().equals("player")) {
			if (event.getSource().getEntity() instanceof Player player) {
				CompoundTag playerData = player.getPersistentData();
				Random rand = new Random();

				ResourceLocation entityLocation = ForgeRegistries.ENTITIES.getKey(livingEntity.getType());
				String lastMobString = playerData.getString(Reference.LAST_MOB_TAG);
				ResourceLocation lastMob = lastMobString.isEmpty() ? null : new ResourceLocation(lastMobString);
				if (entityLocation != null && entityLocation.equals(lastMob)) {
					if (playerData.getBoolean(Reference.AT_RISK_TAG)) {
						if (rand.nextInt(10) <= RallyConfig.COMMON.regainChance.get()) {
							float heal = playerData.getFloat(Reference.LAST_DAMAGE_TAG);
							int actuallyGained = Math.max(1, (int) (heal * RallyConfig.COMMON.regainPercentage.get()));
							player.heal(actuallyGained);
							playerData.putBoolean(Reference.AT_RISK_TAG, false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void riskEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Player player = event.player;
			Level level = player.level;
			MinecraftServer server = level.getServer();
			if (server != null) {
				List<ServerPlayer> playerList = server.getPlayerList().getPlayers();

				final int maxTime = (int) (RallyConfig.COMMON.riskTimer.get() * 20);
				for (Player players : playerList) {
					CompoundTag playerData = players.getPersistentData();


					if (playerData.getBoolean(Reference.AT_RISK_TAG)) {
						int riskTime = playerData.getInt(Reference.RISK_TIME_TAG);
						if (riskTime >= maxTime) {
							riskTime = 0;
							playerData.putInt(Reference.RISK_TIME_TAG, riskTime);
							playerData.putBoolean(Reference.AT_RISK_TAG, false);
						} else {
							riskTime++;
							playerData.putInt(Reference.RISK_TIME_TAG, riskTime);
						}
					} else {
						playerData.putInt(Reference.RISK_TIME_TAG, 0);
					}
				}
			}
		}
	}
}
