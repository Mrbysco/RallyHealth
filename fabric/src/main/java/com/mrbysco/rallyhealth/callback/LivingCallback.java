package com.mrbysco.rallyhealth.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingCallback {
	public static Event<LivingHurt> HURT_EVENT = EventFactory.createArrayBacked(LivingHurt.class,
			(listeners) -> (living, source, amount) -> {
				for (LivingHurt event : listeners) {
					InteractionResult result = event.hurt(living, source, amount);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);
	public static Event<LivingAttack> ATTACK_EVENT = EventFactory.createArrayBacked(LivingAttack.class,
			(listeners) -> (living, source, amount) -> {
				for (LivingAttack event : listeners) {
					InteractionResult result = event.attack(living, source, amount);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	public interface LivingHurt {
		InteractionResult hurt(LivingEntity livingEntity, DamageSource source, float amount);
	}

	public interface LivingAttack {
		InteractionResult attack(LivingEntity livingEntity, DamageSource source, float amount);
	}
}
