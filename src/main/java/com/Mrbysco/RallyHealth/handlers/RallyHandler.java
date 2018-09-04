
package com.Mrbysco.RallyHealth.handlers;

import java.util.List;
import java.util.Random;

import com.Mrbysco.RallyHealth.config.RallyConfigGen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RallyHandler {

	@SubscribeEvent
	public void DamageHandler(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			NBTTagCompound playerData = player.getEntityData();
			Entity trueSource = event.getSource().getTrueSource();
			
			if(trueSource != null && !player.world.isRemote)
			{
				String damageMob = event.getSource().getTrueSource().getName();
				float damageAmount = event.getAmount();
				
				playerData.setFloat("lastDamage", damageAmount);
				playerData.setBoolean("atRisk", true);
				playerData.setInteger("riskTime", 0);
				playerData.setString("lastMob", damageMob);
			}
		}
	}
	
	@SubscribeEvent
	public void livingAttack(LivingAttackEvent event)
	{
		if(event.getSource().getDamageType() == "player")
		{
			if(event.getSource().getTrueSource() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
				NBTTagCompound playerData = player.getEntityData();
				Random rand = new Random();
				
				if(event.getEntityLiving().getName() == playerData.getString("lastMob") && !player.world.isRemote)
				{
					if(playerData.getBoolean("atRisk"))
					{
						if(rand.nextInt(10) < 7)
						{
							float heal = playerData.getFloat("lastDamage");
							
							player.heal(heal);
							playerData.setBoolean("atRisk", false);
						}
					}
				}
			}
		}
	}
		
	@SubscribeEvent
	public void riskEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			MinecraftServer server = world.getMinecraftServer();
			List<EntityPlayerMP> playerList = server.getPlayerList().getPlayers();

			int maxTime = (int) (RallyConfigGen.general.riskTimer * 20);
			for (EntityPlayer players : playerList)
			{
				NBTTagCompound playerData = players.getEntityData();

				int riskTime = playerData.getInteger("riskTime");
				
				if(playerData.getBoolean("atRisk"))
				{
					if(riskTime >= maxTime)
					{
						riskTime = 0;
						playerData.setInteger("riskTime", 0);
						playerData.setBoolean("atRisk", false);
					}
					else
					{
						riskTime++;
						playerData.setInteger("riskTime", riskTime);
					}
				}
				else
				{
					riskTime = 0;
					playerData.setInteger("riskTime", 0);
				}
			}
		}
	}
}
