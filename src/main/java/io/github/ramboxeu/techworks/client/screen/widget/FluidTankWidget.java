package io.github.ramboxeu.techworks.client.screen.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.util.Color;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class FluidTankWidget extends BaseWidget {
    private static final ResourceLocation TANK_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/tank.png");

    private FluidStack storedFluid;
    private int maxStorage;

    public FluidTankWidget(int x, int y, FluidStack storedFluid, int maxStorage) {
        super(x, y, 18, 56);

        this.storedFluid = storedFluid;
        this.maxStorage = maxStorage;
    }

    public FluidTankWidget(int x, int y) {
        this(x, y, FluidStack.EMPTY, 0);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().textureManager.bindTexture(TANK_TEXTURE);
        blit(stack, x, y, 0, 0, width, height, width, height);

        if (!storedFluid.isEmpty()) {
            float level = (float) storedFluid.getAmount() / maxStorage;
            int height  = (int) ((this.height - 2) * level);
            int tankY = y + (this.height - height) - 1;

            FluidAttributes attributes = storedFluid.getFluid().getAttributes();

            Color color = Color.fromRGBA(attributes.getColor(storedFluid));
            ResourceLocation stillTexture = attributes.getStillTexture(storedFluid);

            stack.push();
            RenderUtils.drawFluid(stack, x + 1, tankY, width - 2, height, color, stillTexture);
            stack.pop();
        }
    }

    @Override
    public void renderTooltip(MatrixStack stack, int mouseX, int mouseY, int width, int height) {
        StringBuilder builder = new StringBuilder(Utils.getFluidName(storedFluid).getString()).append(" ");

        if (Screen.hasShiftDown()) {
            builder.append(storedFluid.getAmount() / (float) FluidAttributes.BUCKET_VOLUME).append("b");
        } else {
            builder.append(storedFluid.getAmount()).append("mb");
        }

        GuiUtils.drawHoveringText(stack, Lists.newArrayList(ITextProperties.func_240652_a_(builder.toString())), mouseX, mouseY, width, height, -1, Minecraft.getInstance().fontRenderer);
    }

    public void setStoredFluid(FluidStack storedFluid) {
        this.storedFluid = storedFluid;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }
}
