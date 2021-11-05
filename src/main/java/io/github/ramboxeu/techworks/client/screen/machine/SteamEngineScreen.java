package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SteamEngineScreen extends BaseMachineScreen<SteamEngineTile, SteamEngineContainer> {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/steam_engine.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/steam_engine_front_off");

    public SteamEngineScreen(SteamEngineContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn, TEX, MACHINE_FRONT);
    }
}
