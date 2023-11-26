package com.mrbysco.rallyhealth;

import com.mrbysco.rallyhealth.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RallyData extends SavedData {
	private static final String DATA_NAME = Constants.MOD_ID + "_world_data";
	private static final Map<UUID, RallyInfo> infoMap = new HashMap<>();

	public RallyData() {
	}

	public static RallyData load(CompoundTag tag) {
		infoMap.clear();
		ListTag infoList = tag.getList("InfoList", CompoundTag.TAG_COMPOUND);
		for (int i = 0; i < infoList.size(); ++i) {
			CompoundTag compoundTag = infoList.getCompound(i);
			UUID uuid = compoundTag.getUUID("User");
			CompoundTag infoTag = compoundTag.getCompound("Info");
			RallyInfo info = RallyInfo.read(infoTag);

			infoMap.put(uuid, info);
		}
		return new RallyData();
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		ListTag infoList = new ListTag();
		for (Map.Entry<UUID, RallyInfo> entry : infoMap.entrySet()) {
			CompoundTag compoundTag = new CompoundTag();
			compoundTag.putUUID("User", entry.getKey());
			compoundTag.put("Info", entry.getValue().save(new CompoundTag()));
			infoList.add(compoundTag);
		}
		tag.put("InfoList", infoList);

		return tag;
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
		return storage.computeIfAbsent(new SavedData.Factory<>(RallyData::new, RallyData::load, null), DATA_NAME);
	}
}
