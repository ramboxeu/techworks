package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.SolidFuelBurnerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SolidFuelBurnerScreen extends BaseScreen<SolidFuelBurnerContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/solid_fuel_burner.png");

    public SolidFuelBurnerScreen(SolidFuelBurnerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEXTURE);
    }
}
