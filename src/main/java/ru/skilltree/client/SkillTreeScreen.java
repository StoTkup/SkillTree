
package ru.skilltree.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkillTreeScreen extends Screen {

    private static final ResourceLocation COSMIC_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "skilltree",
                    "textures/gui/cosmic_background.png"
            );

    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SOUL_COLOR = 0xFF65E8DF;

    public SkillTreeScreen() {
        super(Component.literal("Древо талантов"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ModKeyMappings.OPEN_SKILL_TREE.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(
                COSMIC_BACKGROUND,
                0, 0, 0,
                0.0F, 0.0F,
                width, height,
                1920, 1080
        );

        graphics.drawCenteredString(
                font,
                "ДРЕВО ТАЛАНТОВ",
                width / 2,
                18,
                TEXT_COLOR
        );

        graphics.drawString(
                font,
                "◆ Души: 0",
                width - 100,
                18,
                SOUL_COLOR,
                true
        );

        int centerX = width / 2;
        int centerY = height / 2;

        graphics.fill(
                centerX - 18, centerY - 18,
                centerX + 18, centerY + 18,
                0x5537D9D0
        );

        graphics.fill(
                centerX - 11, centerY - 11,
                centerX + 11, centerY + 11,
                0xAA38CFCB
        );

        graphics.fill(
                centerX - 5, centerY - 5,
                centerX + 5, centerY + 5,
                0xFFFFFFFF
        );

        graphics.drawCenteredString(
                font,
                "КОСМИЧЕСКОЕ ЯДРО",
                centerX,
                centerY + 28,
                TEXT_COLOR
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
