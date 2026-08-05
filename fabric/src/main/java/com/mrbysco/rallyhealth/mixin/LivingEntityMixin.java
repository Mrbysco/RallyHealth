package com.mrbysco.rallyhealth.mixin;

import com.mrbysco.rallyhealth.callback.LivingCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
			shift = At.Shift.BEFORE,
			ordinal = 0), cancellable = true)
	public void actuallyHurt(ServerLevel level, DamageSource source, float dmg, CallbackInfo ci) {
		if (LivingCallback.HURT_EVENT.invoker().hurt((LivingEntity) (Object) this, source, dmg) == InteractionResult.FAIL) {
			ci.cancel();
		}
	}

	@Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	public void hurt(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (LivingCallback.ATTACK_EVENT.invoker().attack((LivingEntity) (Object) this, source, damage) == InteractionResult.FAIL) {
			cir.setReturnValue(false);
		}
	}
}