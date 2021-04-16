package io.github.ramboxeu.techworks.common.util;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.UUID;

public class NBTUtils {

    @Nullable
    public static UUID getNetworkId(CompoundNBT tag) {
        if (tag.contains("NetworkId")) {
            return tag.getUniqueId("NetworkId");
        }

        return null;
    }
}
