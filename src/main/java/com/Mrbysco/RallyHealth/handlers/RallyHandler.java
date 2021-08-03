
package com.mrbysco.rallyhealth.handlers;

import com.mrbysco.rallyhealth.Reference;
import com.mrbysco.rallyhealth.config.RallyConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class RallyHandler {
	
	@SubscribeEvent
	public void DamageHandler(LivingHurtEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		if(!livingEntity.level.isClientSide && livingEntity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntityLiving();
			CompoundNBT playerData = player.getPersistentData();
			DamageSource source = event.getSource();
			Entity trueSource = event.getSource().getEntity();
			
			if(trueSource != null) {
				ResourceLocation mobLoc = trueSource.getType().getRegistryName();
				String damageMob = mobLoc != null ? mobLoc.toString() : "";
				float damageAmount = event.getAmount();
				if (damageAmount <= 0) return;

				if(!source.isBypassArmor()) {
					damageAmount = CombatRules.getDamageAfterAbsorb(damageAmount, (float)player.getArmorValue(), (float)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
				}
				int k = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), source);
				if (k > 0) {
					damageAmount = CombatRules.getDamageAfterMagicAbsorb(damageAmount, (float)k);
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
		if(!livingEntity.level.isClientSide && event.getSource().getMsgId().equals("player")) {
			if(event.getSource().getEntity() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getSource().getEntity();
				CompoundNBT playerData = player.getPersistentData();
				Random rand = new Random();

				ResourceLocation entityLocation = livingEntity.getType().getRegistryName();
				String lastMobString = playerData.getString(Reference.LAST_MOB_TAG);
				ResourceLocation lastMob = lastMobString.isEmpty() ? null : new ResourceLocation(lastMobString);
				if(entityLocation != null && entityLocation.equals(lastMob)) {
					if(playerData.getBoolean(Reference.AT_RISK_TAG)) {
						if(rand.nextInt(10) <= RallyConfig.COMMON.regainChance.get()) {
							float heal = playerData.getFloat(Reference.LAST_DAMAGE_TAG);
							int actuallyGained = Math.max(1, (int)(heal * RallyConfig.COMMON.regainPercentage.get()));
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
			PlayerEntity player = event.player;
			World level = player.level;
			MinecraftServer server = level.getServer();
			List<ServerPlayerEntity> playerList = server.getPlayerList().getPlayers();

			final int maxTime = (int) (RallyConfig.COMMON.riskTimer.get() * 20);
			for (PlayerEntity players : playerList) {
				CompoundNBT playerData = players.getPersistentData();

				int riskTime = playerData.getInt(Reference.RISK_TIME_TAG);
				
				if(playerData.getBoolean(Reference.AT_RISK_TAG)) {
					if(riskTime >= maxTime) {
						riskTime = 0;
						playerData.putInt(Reference.RISK_TIME_TAG, riskTime);
						playerData.putBoolean(Reference.AT_RISK_TAG, false);
					} else {
						riskTime++;
						playerData.putInt(Reference.RISK_TIME_TAG, riskTime);
					}
				} else {
					riskTime = 0;
					playerData.putInt(Reference.RISK_TIME_TAG, riskTime);
				}
			}
		}
	}
}
