
package com.mrbysco.rallyhealth.handlers;

import com.mrbysco.rallyhealth.config.RallyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
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
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntityLiving();
			CompoundNBT playerData = player.getPersistentData();
			Entity trueSource = event.getSource().getTrueSource();
			
			if(trueSource != null && !player.world.isRemote) {
				ResourceLocation mobLoc = trueSource.getType().getRegistryName();
				String damageMob = mobLoc != null ? mobLoc.toString() : "";
				float damageAmount = event.getAmount();
				
				playerData.putFloat("lastDamage", damageAmount);
				playerData.putBoolean("atRisk", true);
				playerData.putInt("riskTime", 0);
				playerData.putString("lastMob", damageMob);
			}
		}
	}
	
	@SubscribeEvent
	public void livingAttack(LivingAttackEvent event) {
		if(event.getSource().getDamageType().equals("player")) {
			if(event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
				CompoundNBT playerData = player.getPersistentData();
				Random rand = new Random();

				ResourceLocation entityLocation = event.getEntityLiving().getType().getRegistryName();
				String lastMobString = playerData.getString("lastMob");
				ResourceLocation lastMob = lastMobString.isEmpty() ? null : new ResourceLocation(playerData.getString("lastMob"));
				if(entityLocation != null && entityLocation.equals(lastMob) && !player.world.isRemote) {
					if(playerData.getBoolean("atRisk")) {
						if(rand.nextInt(10) < 7) {
							float heal = playerData.getFloat("lastDamage");
							
							player.heal(heal);
							playerData.putBoolean("atRisk", false);
						}
					}
				}
			}
		}
	}
		
	@SubscribeEvent
	public void riskEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;
			MinecraftServer server = world.getServer();
			List<ServerPlayerEntity> playerList = server.getPlayerList().getPlayers();

			final int maxTime = (int) (RallyConfig.COMMON.riskTimer.get() * 20);
			for (PlayerEntity players : playerList) {
				CompoundNBT playerData = players.getPersistentData();

				int riskTime = playerData.getInt("riskTime");
				
				if(playerData.getBoolean("atRisk")) {
					if(riskTime >= maxTime) {
						riskTime = 0;
						playerData.putInt("riskTime", riskTime);
						playerData.putBoolean("atRisk", false);
					} else {
						riskTime++;
						playerData.putInt("riskTime", riskTime);
					}
				} else {
					riskTime = 0;
					playerData.putInt("riskTime", riskTime);
				}
			}
		}
	}
}
