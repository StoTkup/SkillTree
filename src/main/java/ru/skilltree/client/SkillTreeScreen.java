
package ru.skilltree.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SkillTreeScreen extends Screen {

    private static final int BACKGROUND_COLOR = 0xE6101518;
    private static final int PANEL_COLOR = 0xFF172326;
    private static final int BORDER_COLOR = 0xFF247C78;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SOUL_COLOR = 0xFF20A6A0;

    public SkillTreeScreen() {
        super(Component.literal("Древо талантов"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        graphics.fill(0, 0, width, height, 0xFF000000);

        int left = width / 2 - 180;
        int top = height / 2 - 110;
        int right = width / 2 + 180;
        int bottom = height / 2 + 110;

        // Основной фон
        graphics.fill(
                left,
                top,
                right,
                bottom,
                BACKGROUND_COLOR
        );

        // Рамка
        graphics.fill(left, top, right, top + 2, BORDER_COLOR);
        graphics.fill(left, bottom - 2, right, bottom, BORDER_COLOR);
        graphics.fill(left, top, left + 2, bottom, BORDER_COLOR);
        graphics.fill(right - 2, top, right, bottom, BORDER_COLOR);

        // Панель заголовка
        graphics.fill(
                left + 3,
                top + 3,
                right - 3,
                top + 30,
                PANEL_COLOR
        );

        // Название
        graphics.drawCenteredString(
                font,
                "ДРЕВО ТАЛАНТОВ",
                width / 2,
                top + 11,
                TEXT_COLOR
        );

        // Количество душ
        graphics.drawString(
                font,
                "◆ Души: 0",
                right - 95,
                top + 38,
                SOUL_COLOR,
                false
        );

        // Центральная область древа
        graphics.fill(
                left + 15,
                top + 55,
                right - 15,
                bottom - 15,
                PANEL_COLOR
        );

        // Центральный узел
        int centerX = width / 2;
        int centerY = height / 2 + 15;

        graphics.fill(
                centerX - 12,
                centerY - 12,
                centerX + 12,
                centerY + 12,
                BORDER_COLOR
        );

        graphics.drawCenteredString(
                font,
                "ЦЕНТРАЛЬНЫЙ УЗЕЛ",
                centerX,
                centerY + 25,
                TEXT_COLOR
        );

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
