package com.mrbysco.rallyhealth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record RallyInfo(long time, float damage, ResourceLocation mob) {
	public static final Codec<RallyInfo> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(Codec.LONG.fieldOf("time").forGetter(RallyInfo::time),
							Codec.FLOAT.fieldOf("damage").forGetter(RallyInfo::damage),
							ResourceLocation.CODEC.fieldOf("mob").forGetter(RallyInfo::mob))
					.apply(inst, RallyInfo::new));
}
