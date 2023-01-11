package com.mrbysco.rallyhealth.mixin;

import com.mrbysco.rallyhealth.callback.LivingCallback;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin {

	@Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (LivingCallback.ATTACK_EVENT.invoker().attack((LivingEntity) (Object) this, source, amount) == InteractionResult.FAIL) {
			cir.setReturnValue(false);
		}
	}
}