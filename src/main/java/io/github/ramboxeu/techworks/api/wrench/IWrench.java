package io.github.ramboxeu.techworks.api.wrench;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for external wrenches. Saves writing code that this mod already includes.
 *<br />
 *
 * Techworks takes care of running appropriate methods on it's own, when item implements this.
 */
public interface IWrench {
    /**
     * Called when user right clicks a block, that is {@link IWrenchable}
     * @param player the player that used the item
     * @return action which should be executed
     */
    default WrenchAction rightClick(PlayerEntity player) {
        if (player.isSneaking()) {
            return WrenchAction.DISMANTLE;
        } else {
            return WrenchAction.ROTATE;
        }
    }

    /**
     * Called when user left clicks a block, that is {@link IWrenchable}
     * @param player the player that used the item
     * @return action which should be executed
     */
    default WrenchAction leftClick(PlayerEntity player) {
        if (player.isSneaking()) {
            return WrenchAction.NONE;
        } else {
            return WrenchAction.CONFIGURE;
        }
    }
}
