package com.mrbysco.rallyhealth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record RallyInfo(long time, float damage, Identifier mob) {
	public static final Codec<RallyInfo> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(Codec.LONG.fieldOf("time").forGetter(RallyInfo::time),
							Codec.FLOAT.fieldOf("damage").forGetter(RallyInfo::damage),
							Identifier.CODEC.fieldOf("mob").forGetter(RallyInfo::mob))
					.apply(inst, RallyInfo::new));
}
