package com.mrbysco.rallyhealth;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class RallyInfo {
	private final long time;
	private final float damage;
	private final ResourceLocation mob;

	public RallyInfo(long time, float damage, ResourceLocation mob) {
		this.time = time;
		this.damage = damage;
		this.mob = mob;
	}

	public RallyInfo(long time, float damage, String mobString) {
		ResourceLocation mobLocation = ResourceLocation.tryParse(mobString);
		if (mobLocation == null) {
			throw new NullPointerException("Invalid mob location");
		}
		this.time = time;
		this.damage = damage;
		this.mob = mobLocation;
	}

	public long time() {
		return time;
	}

	public float damage() {
		return damage;
	}

	public ResourceLocation mob() {
		return mob;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (RallyInfo) obj;
		return this.time == that.time &&
				Float.floatToIntBits(this.damage) == Float.floatToIntBits(that.damage) &&
				Objects.equals(this.mob, that.mob);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time, damage, mob);
	}

	@Override
	public String toString() {
		return "RallyInfo[" +
				"time=" + time + ", " +
				"damage=" + damage + ", " +
				"mob=" + mob + ']';
	}

	public CompoundTag save(CompoundTag tag) {
		tag.putLong(Constants.RISK_TIME_TAG, time);
		tag.putFloat(Constants.LAST_DAMAGE_TAG, damage);
		tag.putString(Constants.LAST_MOB_TAG, mob.toString());
		return tag;
	}

	public static RallyInfo read(CompoundTag tag) {
		return new RallyInfo(
				tag.getLong(Constants.RISK_TIME_TAG),
				tag.getFloat(Constants.LAST_DAMAGE_TAG),
				tag.getString(Constants.LAST_MOB_TAG)
		);
	}

}
