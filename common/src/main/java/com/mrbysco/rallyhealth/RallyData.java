package com.mrbysco.rallyhealth;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.rallyhealth.platform.Services;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RallyData extends SavedData {
	private static final String DATA_NAME = Constants.MOD_ID + "_world_data";

	public static final Codec<RallyData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.unboundedMap(UUIDUtil.CODEC, RallyInfo.CODEC).fieldOf("infoMap").forGetter(data -> data.infoMap))
			.apply(inst, RallyData::new));

	private final Map<UUID, RallyInfo> infoMap;

	public RallyData() {
		this(Maps.newHashMap());
	}

	public RallyData(Map<UUID, RallyInfo> infoMap) {
		this.infoMap = Maps.newHashMap(infoMap);
	}

	public static SavedDataType<RallyData> type() {
		return new SavedDataType<>(DATA_NAME, RallyData::new, CODEC, null);
	}

	public RallyInfo getInfo(UUID uuid) {
		return infoMap.getOrDefault(uuid, null);
	}

	public void removeInfo(UUID uuid) {
		infoMap.remove(uuid);
	}

	public void putInfo(UUID uuid, RallyInfo info) {
		infoMap.put(uuid, info);
	}

	public boolean isWithinRiskTimer(UUID uuid, Long currentTime) {
		RallyInfo info = infoMap.getOrDefault(uuid, null);
		if (info != null) {
			Long oldTime = info.time();
			if (currentTime < oldTime) {
				Constants.LOGGER.error("Skipping risk timer check as the current time {} is earlier than the damage time {}", oldTime, currentTime);
				return false;
			}
			int timePassed = (int) (currentTime - oldTime);
			if (timePassed <= Services.PLATFORM.getRiskTimer()) {
				return true;
			}
		}
		return false;
	}

	public static RallyData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(type());
	}
}
