package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.client.container.AssemblyTableContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AssemblyTableScreen extends BaseScreen<AssemblyTableContainer> {
    private static final ResourceLocation BACKGROUND = guiTexture("assembly_table");

    public AssemblyTableScreen(AssemblyTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, BACKGROUND);

        ySize = 186;
        playerInventoryTitleY = ySize - 94; // Recalculate players inv title y with the new ySize
    }
}
