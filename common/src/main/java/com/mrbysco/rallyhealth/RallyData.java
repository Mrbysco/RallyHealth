package com.mrbysco.rallyhealth;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.rallyhealth.config.RallyConfig;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

import java.util.Map;
import java.util.UUID;

public class RallyData extends SavedData {
	private static final Identifier DATA_NAME = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "rally_data");

	public static final Codec<RallyData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.unboundedMap(UUIDUtil.STRING_CODEC, RallyInfo.CODEC).fieldOf("infoMap").forGetter(data -> data.infoMap))
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
			return timePassed <= RallyConfig.COMMON.riskTimer.get();
		}
		return false;
	}

	public static RallyData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		SavedDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(type());
	}
}
