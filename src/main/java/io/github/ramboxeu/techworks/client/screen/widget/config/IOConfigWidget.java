package io.github.ramboxeu.techworks.client.screen.widget.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.function.Function;

public class IOConfigWidget extends BaseConfigWidget {
    private static final ResourceLocation SIDE_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "block/machine_side");
    private static final ResourceLocation TOP_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "block/machine_top");
    private static final ResourceLocation BOTTOM_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "block/machine_bottom");

    private static final Style DEFAULT_STYLE = Style.EMPTY.setColor(Color.fromInt(0xFF7F7F7F));

    private static final Style TYPE_STYLE = Style.EMPTY.setColor(Color.fromInt(0xFFCC5F28));
    private static final TranslationTextComponent NONE_TEXT = TranslationKeys.NONE_INPUT_TYPE.text();
    private static final TranslationTextComponent ITEMS_TEXT = TranslationKeys.ITEM_INPUT_TYPE.styledText(TYPE_STYLE);
    private static final TranslationTextComponent LIQUID_TEXT = TranslationKeys.LIQUID_INPUT_TYPE.styledText(TYPE_STYLE);
    private static final TranslationTextComponent GAS_TEXT = TranslationKeys.GAS_INPUT_TYPE.styledText(TYPE_STYLE);
    private static final TranslationTextComponent ENERGY_TEXT = TranslationKeys.ENERGY_INPUT_TYPE.styledText(TYPE_STYLE);

    private static final Style SIDE_STYLE = Style.EMPTY.setColor(Color.fromInt(0xFF999999));
    private static final TranslationTextComponent BOTTOM_TEXT = TranslationKeys.BOTTOM_SIDE.styledText(SIDE_STYLE);
    private static final TranslationTextComponent TOP_TEXT = TranslationKeys.TOP_SIDE.styledText(SIDE_STYLE);
    private static final TranslationTextComponent FRONT_TEXT = TranslationKeys.FRONT_SIDE.styledText(SIDE_STYLE);
    private static final TranslationTextComponent LEFT_TEXT = TranslationKeys.LEFT_SIDE.styledText(SIDE_STYLE);
    private static final TranslationTextComponent RIGHT_TEXT = TranslationKeys.RIGHT_SIDE.styledText(SIDE_STYLE);
    private static final TranslationTextComponent BACK_TEXT = TranslationKeys.BACK_SIDE.styledText(SIDE_STYLE);

    private static final Style MODE_STYLE = Style.EMPTY.setColor(Color.fromInt(0xFFA5A0A0));
    private static final TranslationTextComponent INPUT_TEXT = TranslationKeys.INPUT.styledText(MODE_STYLE);
    private static final TranslationTextComponent OUTPUT_TEXT = TranslationKeys.OUTPUT.styledText(MODE_STYLE);
    private static final TranslationTextComponent BOTH_TEXT = TranslationKeys.BOTH.styledText(MODE_STYLE);

    private final ResourceLocation frontTexture;
    private final BaseMachineContainer<?> container;
    private final List<HandlerData> dataList;
    private final MachineIO io;
    private final int[] colors;

    private int modeTextWidth;
    private int colorBoxSelectionX;
    private int colorBoxSelectionY;
    private int sideSelectionX;
    private int sideSelectionY;
    private int colorBoxIndex;
    private int modeIconOffset;
    private Side side;
    private boolean enabled;
    private ITextComponent info;
    private ITextComponent type;
    private StorageMode mode;
    private HandlerData selectedData;
    private HandlerConfig selectedConfig;

    public IOConfigWidget(BaseMachineScreen<?, ?> screen, ResourceLocation frontTexture) {
        super(screen, TranslationKeys.IO.text());
        this.frontTexture = frontTexture;

        container = screen.getContainer();
        dataList = screen.getContainer().getDataList();
        io = screen.getContainer().getMachineTile().getMachineIO();
        colors = dataList.stream().mapToInt(HandlerData::getColor).toArray();

        colorBoxSelectionX = 67;
        colorBoxSelectionY = 9;
        sideSelectionX = 25;
        sideSelectionY = 30;
        colorBoxIndex = 0;
        side = Side.FRONT;
        updateInfo();
    }

    @Override
    public void onScreenInit(Minecraft minecraft, int guiLeft, int guiTop, int guiWidth, int guiHeight) {
        super.onScreenInit(minecraft, guiLeft, guiTop, guiWidth, guiHeight);
        modeTextWidth = minecraft.fontRenderer.getStringWidth(TranslationKeys.MODE.text().getString());
    }

    @Override
    protected void renderConfig(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (!enabled) {
            blit(stack, 7, 90, 194, 0, 9, 9);
        } else {
            blit(stack, 7, 90, 185, 0, 9, 9);
        }

        blit(stack, modeTextWidth + 13, 109, 203, 0, 53, 17);
        blit(stack, modeTextWidth + 51, 113, 155 + modeIconOffset, 0, 10, 9);

        int colorBoxRow = 0;
        for (int i = 0; i < colors.length; i++) {
            int colorBoxCol = i % 4;

            if (i > 0 && i % 4 == 0) {
                colorBoxRow++;
            }

            fill(stack, 68 + (15 * colorBoxCol), 10 + (15 * colorBoxRow), 78 + (15 * colorBoxCol), 20 + (15 * colorBoxRow), colors[i]);
        }


        fill(stack, colorBoxSelectionX, colorBoxSelectionY, colorBoxSelectionX + 12, colorBoxSelectionY + 1, 0xFFFFFFFF);
        fill(stack, colorBoxSelectionX, colorBoxSelectionY + 1, colorBoxSelectionX + 1, colorBoxSelectionY + 11, 0xFFFFFFFF);
        fill(stack, colorBoxSelectionX + 11, colorBoxSelectionY + 1, colorBoxSelectionX + 12, colorBoxSelectionY + 11, 0xFFFFFFFF);
        fill(stack, colorBoxSelectionX, colorBoxSelectionY + 11, colorBoxSelectionX + 12, colorBoxSelectionY + 12, 0xFFFFFFFF);

        fill(stack, sideSelectionX, sideSelectionY, sideSelectionX + 18, sideSelectionY + 1, 0xFFFFFFFF);
        fill(stack, sideSelectionX, sideSelectionY + 1, sideSelectionX + 1, sideSelectionY + 17, 0xFFFFFFFF);
        fill(stack, sideSelectionX + 17, sideSelectionY + 1, sideSelectionX + 18, sideSelectionY + 17, 0xFFFFFFFF);
        fill(stack, sideSelectionX, sideSelectionY + 17, sideSelectionX + 18, sideSelectionY + 18, 0xFFFFFFFF);

        minecraft.textureManager.bindTexture(RenderUtils.BLOCK_ATLAS_LOC);
        // TAS caching?
        Function<ResourceLocation, TextureAtlasSprite> sprites = minecraft.getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
        TextureAtlasSprite machineSideSprite = sprites.apply(SIDE_TEXTURE);

        blit(stack, 5, 31, 0, 16, 16, machineSideSprite);
        blit(stack, 26, 31, 0, 16, 16, sprites.apply(frontTexture));
        blit(stack, 47, 31, 0, 16, 16, machineSideSprite);
        blit(stack, 26, 10, 0, 16, 16, sprites.apply(TOP_TEXTURE));
        blit(stack, 26, 52, 0, 16, 16, sprites.apply(BOTTOM_TEXTURE));
        blit(stack, 47, 52, 0, 16, 16, machineSideSprite);

        ClientUtils.drawString(stack, minecraft.fontRenderer, info, 7, 76, false);
        ClientUtils.drawString(stack, minecraft.fontRenderer, TranslationKeys.ENABLED.styledText(DEFAULT_STYLE), 20, 91, false);
        ClientUtils.drawString(stack, minecraft.fontRenderer, TranslationKeys.MODE.styledText(DEFAULT_STYLE).appendString(": "), 7, 113, false);
        ClientUtils.drawString(stack, minecraft.fontRenderer, type, 18 + modeTextWidth, 114, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (render) {
            int x = (int) (mouseX - guiLeft);
            int y = (int) (mouseY - guiTop);

            if (x >= 5 && y >= 31 && x <= 21 && y <= 47) {
                updateSide(Side.LEFT, 4, 30);

                return true;
            } else if (x >= 26 && y >= 31 && x <= 42 && y <= 47) {
                updateSide(Side.FRONT, 25, 30);

                return true;
            } else if (x >= 47 && y >= 31 && x <= 63 && y <= 47) {
                updateSide(Side.RIGHT, 46, 30);

                return true;
            } else if (x >= 47 && y >= 52 && x <= 63 && y <= 68) {
                updateSide(Side.BACK, 46, 51);

                return true;
            } else if (x >= 26 && y >= 10 && x <= 42 && y <= 26) {
                updateSide(Side.TOP, 25, 9);

                return true;
            } else if (x >= 26 && y >= 52 && x <= 42 && y <= 68) {
                updateSide(Side.BOTTOM, 25, 51);

                return true;
            } else if (x >= 7 && y >= 90 && x <= 16 && y <= 99) {
                enabled = !enabled;
                playDownSound(Minecraft.getInstance().getSoundHandler());
                container.changeStatus(side, selectedData, mode, enabled);

                return true;
            } else if (x >= modeTextWidth + 13 && y >= 109 && x <= modeTextWidth + 66 && y <= 127) {
                mode = mode.nextNonNone();
                type = getModeText(mode);
                modeIconOffset = nextModeOffset(mode);
                playDownSound(Minecraft.getInstance().getSoundHandler());

                if (selectedConfig != null) {
                    container.changeMode(side, selectedConfig, mode);
                }

                return true;
            } else {
                int colorBoxRow = 0;
                for (int i = 0; i < dataList.size(); i++) {
                    int colorBoxCol = i % 4;

                    if (i > 0 && i % 4 == 0) {
                        colorBoxRow++;
                    }

                    if (x >= 68 + (15 * colorBoxCol) && x <= 78 + (15 * colorBoxCol) && y >= 10 + (15 * colorBoxRow) && y <= 20 + (15 * colorBoxRow)) {
                        if (colorBoxIndex != i) {
                            colorBoxIndex = i;
                            colorBoxSelectionX = 67 + (15 * (i % 4));
                            colorBoxSelectionY = 9 + (15 * (i / 4));
                            playDownSound(Minecraft.getInstance().getSoundHandler());
                            updateInfo();
                        }

                        return true;
                    }
                }
            }

            return false;
        }

        return false;
    }

    private void updateSide(Side side, int selectionX, int selectionY) {
        if (this.side != side) {
            this.side = side;

            sideSelectionX = selectionX;
            sideSelectionY = selectionY;
            playDownSound(Minecraft.getInstance().getSoundHandler());
            updateInfo();
        }
    }

    private void updateInfo() {
        selectedData = dataList.get(colorBoxIndex);
        selectedConfig = io.getHandlerConfig(side, selectedData);

        if (selectedConfig != null) {
            enabled = true;
            mode = selectedConfig.getMode();
            modeIconOffset = nextModeOffset(mode);
        } else {
            enabled = false;
            mode = StorageMode.BOTH;
            modeIconOffset = 20;
        }

        info = TranslationKeys.INFO.styledText(DEFAULT_STYLE)
                .appendString(": ")
                .appendSibling(getSideText(side))
                .appendString(" ")
                .appendSibling(getInputTypeText(selectedData.getType()));

        type = getModeText(mode);
    }

    private static IReorderingProcessor getText(String text) {
        return IReorderingProcessor.fromString(text, DEFAULT_STYLE);
    }

    private static TranslationTextComponent getInputTypeText(InputType type) {
        switch (type) {
            case ITEM:
                return ITEMS_TEXT;
            case LIQUID:
                return LIQUID_TEXT;
            case GAS:
                return GAS_TEXT;
            case ENERGY:
                return ENERGY_TEXT;
            default:
                return NONE_TEXT;
        }
    }

    private static TranslationTextComponent getSideText(Side side) {
        switch (side) {
            case TOP:
                return TOP_TEXT;
            case BOTTOM:
                return BOTTOM_TEXT;
            case FRONT:
                return FRONT_TEXT;
            case LEFT:
                return LEFT_TEXT;
            case RIGHT:
                return RIGHT_TEXT;
            case BACK:
                return BACK_TEXT;
            default:
                return null;
        }
    }

    private static TranslationTextComponent getModeText(StorageMode mode) {
        switch (mode) {
            case INPUT:
                return INPUT_TEXT;
            case OUTPUT:
                return OUTPUT_TEXT;
            case BOTH:
                return BOTH_TEXT;
            default:
                return null;
        }
    }

    private int nextModeOffset(StorageMode mode) {
        switch (mode) {
            case OUTPUT:
                return 10;
            case BOTH:
                return 20;
            default:
                return 0;
        }
    }
}
