//package io.github.ramboxeu.techworks.client.screen;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import io.github.ramboxeu.techworks.Techworks;
//import io.github.ramboxeu.techworks.client.util.RenderUtils;
//import io.github.ramboxeu.techworks.client.util.Utils;
//import io.github.ramboxeu.techworks.common.util.Side;
//import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.ITextProperties;
//
//import java.util.function.Function;
//
//public class IOConfigWidget extends BaseConfigWidget {
//    public final ResourceLocation frontTex;
//
//    private static final ResourceLocation MACHINE_SIDE_TEX = new ResourceLocation(Techworks.MOD_ID, "block/machine_side");
//    private static final ResourceLocation MACHINE_TOP_TEX = new ResourceLocation(Techworks.MOD_ID, "block/machine_top");
//    private static final ResourceLocation MACHINE_BOTTOM_TEX = new ResourceLocation(Techworks.MOD_ID, "block/machine_bottom");
//
//    private static final ResourceLocation CHECKBOX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/checkbox.png");
//    private static final ResourceLocation BUTTON = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/button.png");
//    private static final ResourceLocation ICONS = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/mode_icons.png");
//
//    private static final int SPACING = 5;
//    private static final int SPRITE_X = 5;
//    private static final int SPRITE_Y = 10;
//    private static final int BOX_SIZE = 10;
//    private static final int BOX_X = SPRITE_X + (16 * 3) + (SPACING * 2) + 5; // 5 + 48 + 10 + 5 = 68
//    private static final int BOX_Y = SPRITE_Y;
//    private static final int CONFIG_SCREEN_WIDTH = 130;
//
//    private int boxIndex = 0;
//    private int sideSelectionX = SPRITE_X + SPACING + 16 - 1;
//    private int sideSelectionY = SPRITE_Y + 16 + SPACING - 1;
//    private Side side = Side.FRONT;
//    private StorageMode mode = StorageMode.OUTPUT;
//    private boolean enabledChecked = false;
//    private boolean autoPullChecked = false;
//    private boolean autoPushChecked = false;
//
//    private static final int MAX_LEN = Minecraft.getInstance().fontRenderer.getStringWidth("Output");
//
//    private final int[] colors;
////    private final List<HandlerData> widgets;
//
//    public IOConfigWidget(BaseMachineScreen<?, ?> screen, ResourceLocation frontTex) {
//        super(screen);
//
//        this.frontTex = frontTex;
////        screen.setSide(side);
//
//////        widgets = screen.portWidgets;
////
//////        int length = widgets.size();
////        colors = new int[length];
////
////        for (int i = 0; i < length; i++) {
////            colors[i] = widgets.get(i).getColor();
////        }
//
//        colors = new int[]{ 0xffd74040, 0xffd75940, 0xffd77240, 0xffd78b40, 0xffd7a440 };
//    }
//
//    @Override
//    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
////        Utils.drawString(stack, minecraft.fontRenderer, "I/O CONFIG", 5, 5, 0xFFFFFF, false);
//
//        //<editor-fold desc="Machine side preview render">
//
//        minecraft.textureManager.bindTexture(RenderUtils.BLOCK_ATLAS_LOC);
//        Function<ResourceLocation, TextureAtlasSprite> sprites = minecraft.getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
//        TextureAtlasSprite machineSideSprite = sprites.apply(MACHINE_SIDE_TEX);
//
//        blit(stack, SPRITE_X, SPRITE_Y + 16 + SPACING, 0, 16, 16, machineSideSprite);
//        blit(stack, SPRITE_X + SPACING + 16, SPRITE_Y + 16 + SPACING, 0, 16, 16, sprites.apply(frontTex));
//        blit(stack, SPRITE_X + ((SPACING + 16) * 2), SPRITE_Y + 16 + SPACING, 0, 16, 16, machineSideSprite);
//        blit(stack, SPRITE_X + SPACING + 16, SPRITE_Y, 0, 16, 16, sprites.apply(MACHINE_TOP_TEX));
//        blit(stack, SPRITE_X + SPACING + 16, SPRITE_Y + ((SPACING + 16) * 2), 0, 16, 16, sprites.apply(MACHINE_BOTTOM_TEX));
//        blit(stack, SPRITE_X + ((SPACING + 16) * 2), SPRITE_Y + ((SPACING + 16) * 2), 0, 16, 16, machineSideSprite);
//
//        //</editor-fold>
//
//        //<editor-fold desc="Color chooser render">
//        int b = 0;
//        for (int i = 0; i < 5; i++) {
//            int a = i % 4;
//
//            if (i > 0 && i % 4 == 0) {
//                b++;
//            }
//
//            fill(stack, BOX_X + ((SPACING + BOX_SIZE) * a), BOX_Y + ((SPACING + BOX_SIZE) * b), BOX_X + ((SPACING + BOX_SIZE) * a) + BOX_SIZE, BOX_Y + ((SPACING + BOX_SIZE) * b) + BOX_SIZE, colors[i]);
//        }
//        //</editor-fold>
//
//        //<editor-fold desc="Color selection highlight render">
//        int colorSelectionX = BOX_X + ((SPACING + BOX_SIZE) * (boxIndex % 4)) - 1;
//        int colorSelectionY = BOX_Y + ((SPACING + BOX_SIZE) * (boxIndex / 4)) - 1;
//
//        fill(stack, colorSelectionX, colorSelectionY, colorSelectionX + 12, colorSelectionY + 1, 0xFFFFFFFF);
//        fill(stack, colorSelectionX, colorSelectionY + 1, colorSelectionX + 1, colorSelectionY + 11, 0xFFFFFFFF);
//        fill(stack, colorSelectionX + 11, colorSelectionY + 1, colorSelectionX + 12, colorSelectionY + 11, 0xFFFFFFFF);
//        fill(stack, colorSelectionX, colorSelectionY + 11, colorSelectionX + 12, colorSelectionY + 12, 0xFFFFFFFF);
//        //</editor-fold>
//
//        //<editor-fold desc="Side selection highlight render">
//        fill(stack, sideSelectionX, sideSelectionY, sideSelectionX + 18, sideSelectionY + 1, 0xFFFFFFFF);
//        fill(stack, sideSelectionX, sideSelectionY + 1, sideSelectionX + 1, sideSelectionY + 17, 0xFFFFFFFF);
//        fill(stack, sideSelectionX + 17, sideSelectionY + 1, sideSelectionX + 18, sideSelectionY + 17, 0xFFFFFFFF);
//        fill(stack, sideSelectionX, sideSelectionY + 17, sideSelectionX + 18, sideSelectionY + 18, 0xFFFFFFFF);
//        //</editor-fold>
//
//        minecraft.textureManager.bindTexture(CHECKBOX);
//
//        // Enabled checkbox
//        if (!enabledChecked) {
//            blit(stack, 7, 75 + 15, 0, 9, 0, 9, 9, 18, 18);
//        } else {
//            blit(stack, 7, 75 + 15, 0, 0, 0, 9, 9, 18, 18);
//        }
//
//        if (!autoPullChecked) {
//            blit(stack, 7, 94 + 15, 0, 9, 0, 9, 9, 18, 18);
//        } else {
//            blit(stack, 7, 94 + 15, 0, 0, 0, 9, 9, 18, 18);
//        }
//
//        if (!autoPushChecked) {
//            blit(stack, 7, 106 + 15, 0, 9, 0, 9, 9, 18, 18);
//        } else {
//            blit(stack, 7, 106 + 15, 0, 0, 0, 9, 9, 18, 18);
//        }
//
//        // 106 + 9 + 10
//        minecraft.textureManager.bindTexture(BUTTON);
//        int modeWidth = minecraft.fontRenderer.getStringWidth("Mode:");
////        blit(stack, 7 + modeWidth + 5, 123 + 13, 0, 0, 0, 40, 17, 17, 40);
//        blit(stack, 7 + modeWidth + 5, 123 + 15, MAX_LEN + 6 + 19, 17, 0.0f, 0.0f, 37, 17, 37, 17);
////        blit(stack, 7 + MAX_LEN + 4 + 14, 124 + 11, 37.0f, 0.0f, 3, 17, 3, 17);
//
//        minecraft.textureManager.bindTexture(ICONS);
//        blit(stack, 7 + modeWidth + 5 + 6 + MAX_LEN + 3, 127 + 15, 0, 20, 0, 10, 9, 9, 30);
//
//        // 0xFFCC3028
//        int infoWidth = minecraft.fontRenderer.getStringWidth("Info: ");
//        int frontWidth = minecraft.fontRenderer.getStringWidth("Front, ");
//
//        Utils.drawString(stack, minecraft.fontRenderer, "Info:", 7, 76, 0xFFFFFFFF, false);
//        Utils.drawString(stack, minecraft.fontRenderer, "Front,", 7 + infoWidth, 76, 0xFFFFFFFF, false);
//        Utils.drawString(stack, minecraft.fontRenderer, "Energy", 7 + infoWidth + frontWidth, 76, 0xFFCC3028, false);
//
//        Utils.drawString(stack, minecraft.fontRenderer, "Enabled", 20, 76 + 15, 0xFFFFFFFF, false);
//        Utils.drawString(stack, minecraft.fontRenderer, "Auto-Pull", 20, 94 + 15, 0xFFFFFFFF, false);
//        Utils.drawString(stack, minecraft.fontRenderer, "Auto-Push", 20, 106 + 15, 0xFFFFFFFF, false);
//
//        Utils.drawString(stack, minecraft.fontRenderer, "Mode:", 7, 127 + 15, 0xFFFFFFFF, false);
//        Utils.drawString(stack, minecraft.fontRenderer, "Output", 7 + modeWidth + 5 + 6, 128 + 15, 0xFFFFFFFF, false);
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        int x = (int) (mouseX - screen.getGuiLeft()) + CONFIG_SCREEN_WIDTH;
//        int y = (int) (mouseY - screen.getGuiTop());
//
//        if (x >= SPRITE_X && y >= SPRITE_Y + 16 + SPACING && x <= SPRITE_X + 16 && y <= SPRITE_Y + 16 + SPACING + 16) {
//            Techworks.LOGGER.debug("Left side clicked");
//            sideSelectionX = SPRITE_X - 1;
//            sideSelectionY = SPRITE_Y + 16 + SPACING - 1;
//
//            if (!side.equals(Side.LEFT)) {
//                side = Side.LEFT;
//            }
//
//            return true;
//        } else if (x >= SPRITE_X + ((SPACING + 16) * 2) && y >= SPRITE_Y + 16 + SPACING && x <= SPRITE_X + ((SPACING + 16) * 2) + 16 && y <= SPRITE_Y + 16 + SPACING + 16) {
//            Techworks.LOGGER.debug("Right side clicked");
//            sideSelectionX = SPRITE_X + ((SPACING + 16) * 2) - 1;
//            sideSelectionY = SPRITE_Y + 16 + SPACING - 1;
//
//            if (!side.equals(Side.RIGHT)) {
//                side = Side.RIGHT;
//            }
//
//            return true;
//        } else if (x >= SPRITE_X + ((SPACING + 16) * 2) && y >= SPRITE_Y + ((SPACING + 16) * 2) && x <= SPRITE_X + ((SPACING + 16) * 2) + 16 && y <= SPRITE_Y + ((SPACING + 16) * 2) + 16) {
//            Techworks.LOGGER.debug("Back side clicked");
//            sideSelectionX = SPRITE_X + ((SPACING + 16) * 2) - 1;
//            sideSelectionY = SPRITE_Y + ((SPACING + 16) * 2) - 1;
//
//            if (!side.equals(Side.BACK)) {
//                side = Side.BACK;
//            }
//
//            return true;
//        } else if (x >= SPRITE_X + SPACING + 16 && y >= SPRITE_Y + 16 + SPACING && x <= SPRITE_X + SPACING + 16 + 16 && y <= SPRITE_Y + 16 + SPACING + 16) {
//            Techworks.LOGGER.debug("Front side clicked");
//            sideSelectionX = SPRITE_X + SPACING + 16 - 1;
//            sideSelectionY = SPRITE_Y + 16 + SPACING - 1;
//
//            if (!side.equals(Side.FRONT)) {
//                side = Side.FRONT;
//            }
//
//            return true;
//        } else if (x >= SPRITE_X + SPACING + 16 && y >= SPRITE_Y && x <= SPRITE_X + SPACING + 16 + 16 && y <= SPRITE_Y + 16) {
//            Techworks.LOGGER.debug("Top side clicked");
//            sideSelectionX = SPRITE_X + SPACING + 16 - 1;
//            sideSelectionY = SPRITE_Y - 1;
//
//            if (!side.equals(Side.TOP)) {
//                side = Side.TOP;
//            }
//
//            return true;
//        } else if (x >= SPRITE_X + SPACING + 16 && y >= SPRITE_Y + ((SPACING + 16) * 2) && x <= SPRITE_X + SPACING + 16 + 16 && y <= SPRITE_Y + ((SPACING + 16) * 2) + 16) {
//            Techworks.LOGGER.debug("Bottom side clicked");
//            sideSelectionX = SPRITE_X + SPACING + 16 - 1;
//            sideSelectionY = SPRITE_Y + ((SPACING + 16) * 2) - 1;
//
//            if (!side.equals(Side.BOTTOM)) {
//                side = Side.BOTTOM;
//            }
//
//            return true;
//        } else if (x >= 7 && y >= 75 && x <= 16 && y <= 84) {
//            enabledChecked = !enabledChecked;
//            Techworks.LOGGER.debug("Checkbox clicked!");
//            return true;
//        } else if (x >= 7 && y >= 94 && x <= 16 && y <= 103) {
//            autoPullChecked = !autoPullChecked;
//            Techworks.LOGGER.debug("Checkbox clicked!");
//            return true;
//        } else if (x >= 7 && y >= 106 && x <= 16 && y <= 115) {
//            autoPushChecked = !autoPushChecked;
//            Techworks.LOGGER.debug("Checkbox clicked!");
//            return true;
//        } else {
//
//            int b = 0;
//            for (int i = 0; i < 5; i++) {
//                int a = i % 4;
//
//                if (i > 0 && i % 4 == 0) {
//                    b++;
//                }
//
//                if (x >= BOX_X + ((SPACING + BOX_SIZE) * a) && x <= BOX_X + ((SPACING + BOX_SIZE) * a) + BOX_SIZE && y >= BOX_Y + ((SPACING + BOX_SIZE) * b) && y <= BOX_Y + ((SPACING + BOX_SIZE) * b) + BOX_SIZE) {
//                    boxIndex = i;
//                    Techworks.LOGGER.debug("Pressed: {}", boxIndex);
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//}
