package io.github.ramboxeu.techworks.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientUtils {
    public static final IFormattableTextComponent INPUT_TEXT = new StringTextComponent("Input");

    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 166;

    public static void drawString(MatrixStack stack, FontRenderer font, String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            font.drawStringWithShadow(stack, text, x, y, color);
        } else {
            font.drawString(stack, text, x, y, color);
        }
    }

    public static void drawString(MatrixStack stack, FontRenderer font, ITextComponent text, float x, float y, boolean shadow) {
        if (shadow) {
            font.drawTextWithShadow(stack, text, x, y, 0);
        } else {
            font.drawText(stack, text, x, y, 0);
        }
    }

    public static void drawString(MatrixStack stack, FontRenderer font, IReorderingProcessor text, float x, float y, boolean shadow) {
        if (shadow) {
            font.drawTextWithShadow(stack, text, x, y, 0);
        } else {
            font.func_238422_b_(stack, text, x, y, 0);
        }
    }

    public static void drawCenteredString(MatrixStack stack, FontRenderer font, String text, int x, int y, int color, boolean shadow) {
        drawString(stack, font, text, (float) (x - font.getStringWidth(text) / 2), y, color, shadow);
    }

    public static void drawCenteredString(MatrixStack stack, FontRenderer font, IReorderingProcessor text, int x, int y, boolean shadow) {
        drawString(stack, font, text, (float) (x - font.func_243245_a(text) / 2), y, shadow);
    }

    public static IReorderingProcessor processor(IReorderingProcessor... processors) {
        return IReorderingProcessor.func_242247_b(Arrays.asList(processors));
    }

    public static IReorderingProcessor processor(String text) {
        return IReorderingProcessor.fromString(text, Style.EMPTY);
    }

    public static IReorderingProcessor processor(ITextProperties text) {
        return text.getComponentWithStyle(
                (string, style) -> Optional.of(IReorderingProcessor.fromString(style, string)),
                Style.EMPTY
        ).orElse(IReorderingProcessor.field_242232_a);
    }

    public static void renderTooltip(Screen screen, FontRenderer font, MatrixStack stack, ITextProperties text, int mouseX, int mouseY) {
        screen.renderToolTip(stack, Collections.singletonList(processor(text)), mouseX, mouseY, font);
    }

    public static void renderTooltip(Screen screen, FontRenderer font, MatrixStack stack, List<ITextComponent> props, int mouseX, int mouseY) {
        screen.renderToolTip(stack, props.stream().map(ITextComponent::func_241878_f).collect(Collectors.toList()), mouseX, mouseY, font);
    }
}
