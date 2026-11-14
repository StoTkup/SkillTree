
package ru.skilltree.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;

public class SkillTreeScreen extends Screen {

    private static final ResourceLocation COSMIC_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "skilltree",
                    "textures/gui/cosmic_background.png"
            );

    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SOUL_COLOR = 0xFF65E8DF;

    private static final double MIN_ZOOM = 0.4D;
    private static final double MAX_ZOOM = 2.5D;
    private static final double ZOOM_STEP = 1.1D;

    // Текущее положение камеры
    private double cameraX = 0.0D;
    private double cameraY = 0.0D;
    private double zoom = 1.0D;

    // Целевое положение для плавного движения
    private double targetCameraX = 0.0D;
    private double targetCameraY = 0.0D;
    private double targetZoom = 1.0D;

    private boolean draggingCamera = false;
    private boolean hoveringFirstStrike = false;

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
public boolean mouseClicked(double mouseX, double mouseY, int button) {

    if (button == 0) {
        double treeX = (mouseX - width / 2.0D - cameraX) / zoom
                + width / 2.0D;

        double treeY = (mouseY - height / 2.0D - cameraY) / zoom
                + height / 2.0D;

        int talentX = width / 2;
        int talentY = height / 2 - 85;

        if (treeX >= talentX - 35
                && treeX <= talentX + 35
                && treeY >= talentY - 15
                && treeY <= talentY + 15) {

            if (Minecraft.getInstance().getConnection() != null) {
                Minecraft.getInstance().getConnection().send(
                        new ru.skilltree.network.LearnTalentPacket(
                                "warrior_first_strike"
                        )
                );
            }

            return true;
        }
    }

    if (button == 1) {
        draggingCamera = true;
        return true;
    }

    return super.mouseClicked(mouseX, mouseY, button);
}


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 1) {
            draggingCamera = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY
    ) {
        if (button == 1 && draggingCamera) {
            targetCameraX += dragX;
            targetCameraY += dragY;
            clampCamera();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double scrollX,
            double scrollY
    ) {
        if (scrollY == 0.0D) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        double oldZoom = targetZoom;

        if (scrollY > 0) {
            targetZoom = Math.min(MAX_ZOOM, targetZoom * ZOOM_STEP);
        } else {
            targetZoom = Math.max(MIN_ZOOM, targetZoom / ZOOM_STEP);
        }

        double ratio = targetZoom / oldZoom;

        targetCameraX = mouseX - width / 2.0D
                - (mouseX - width / 2.0D - targetCameraX) * ratio;

        targetCameraY = mouseY - height / 2.0D
                - (mouseY - height / 2.0D - targetCameraY) * ratio;

        clampCamera();
        return true;
    }

    // Ограничиваем перемещение, чтобы фон не открывал пустые края.
    private void clampCamera() {
        double backgroundWidth = getBackgroundWidth();
        double backgroundHeight = getBackgroundHeight();

        double maxX = Math.max(
                0.0D,
                backgroundWidth * targetZoom / 2.0D - width / 2.0D
        );

        double maxY = Math.max(
                0.0D,
                backgroundHeight * targetZoom / 2.0D - height / 2.0D
        );

        targetCameraX = Math.max(-maxX, Math.min(maxX, targetCameraX));
        targetCameraY = Math.max(-maxY, Math.min(maxY, targetCameraY));
    }

    // Фон больше экрана, но сохраняет исходные пропорции 16:9.
    private double getBackgroundWidth() {
        return Math.max(width, height * 16.0D / 9.0D) * 5.0D;
    }

    private double getBackgroundHeight() {
        return getBackgroundWidth() * 9.0D / 16.0D;
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        // Плавное приближение камеры к целевым значениям.
        double smooth = 0.18D;

        cameraX += (targetCameraX - cameraX) * smooth;
        cameraY += (targetCameraY - cameraY) * smooth;
        zoom += (targetZoom - zoom) * smooth;

        graphics.pose().pushPose();

        // Общая камера для фона и древа.
        graphics.pose().translate(
                (float) (width / 2.0D + cameraX),
                (float) (height / 2.0D + cameraY),
                0.0F
        );

        graphics.pose().scale(
                (float) zoom,
                (float) zoom,
                1.0F
        );

        graphics.pose().translate(
                -width / 2.0F,
                -height / 2.0F,
                0.0F
        );

        // Космос теперь движется вместе с камерой.
        int backgroundWidth = (int) getBackgroundWidth();
        int backgroundHeight = (int) getBackgroundHeight();

        graphics.blit(
                COSMIC_BACKGROUND,
        (width - backgroundWidth) / 2,
        (height - backgroundHeight) / 2,
        backgroundWidth,
        backgroundHeight,
        0.0F, 0.0F,
        1920, 1080,
        1920, 1080
);

// Затемняем космический фон.
graphics.fill(
        (width - backgroundWidth) / 2,
        (height - backgroundHeight) / 2,
        (width + backgroundWidth) / 2,
        (height + backgroundHeight) / 2,
        0x66000000
);

        // Древо находится в том же пространстве, что и фон.
        renderSkillTree(graphics, mouseX, mouseY);

        graphics.pose().popPose();

        // HUD остаётся неподвижным поверх космоса.
        graphics.drawCenteredString(
                font,
                "ДРЕВО ТАЛАНТОВ",
                width / 2,
                18,
                TEXT_COLOR
        );

        
int souls = minecraft.player != null
        ? minecraft.player.getData(
                ru.skilltree.attachment.ModAttachments.SOULS.get()
        )
        : 0;

graphics.drawCenteredString(
        font,
        "◆ Души: " + souls,
        width / 2,
        38,
        SOUL_COLOR
);


        graphics.drawCenteredString(
                font,
                "ПКМ — перемещение | Колёсико — масштаб",
                width / 2,
                height - 20,
                0xFFB8D9E8
        );
    }

    


private void renderSkillTree(GuiGraphics graphics, int mouseX, int mouseY) {
    int centerX = width / 2;
    int centerY = height / 2;

    // Космическое ядро
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

    // Проверяем, изучен ли талант
    boolean learned = Minecraft.getInstance().player != null
            && Minecraft.getInstance().player.getData(
                    ru.skilltree.attachment.ModAttachments.LEARNED_TALENTS.get()
            ).contains("warrior_first_strike");

    int talentX = centerX;
    int talentY = centerY - 85;

    int talentColor = learned ? 0xFFFFD54A : 0xCC176B65;

    // Фон значка
    graphics.fill(
            talentX - 24, talentY - 20,
            talentX + 24, talentY + 20,
            talentColor
    );

    // Железный меч
    graphics.renderItem(
            new ItemStack(Items.IRON_SWORD),
            talentX - 19,
            talentY - 8
    );

    // Белый плюс
    graphics.drawString(
            font,
            "+",
            talentX + 5,
            talentY - 4,
            0xFFFFFFFF,
            false
    );

    // Координаты курсора с учётом камеры и масштаба
    double treeMouseX = (mouseX - width / 2.0D - cameraX) / zoom
            + width / 2.0D;

    double treeMouseY = (mouseY - height / 2.0D - cameraY) / zoom
            + height / 2.0D;

    hoveringFirstStrike = treeMouseX >= talentX - 24
            && treeMouseX <= talentX + 24
            && treeMouseY >= talentY - 20
            && treeMouseY <= talentY + 20;

    // Подсказка при наведении
    if (hoveringFirstStrike) {
        int tooltipX = (int) treeMouseX + 12;
        int tooltipY = (int) treeMouseY + 12;

        graphics.fill(
                tooltipX,
                tooltipY,
                tooltipX + 175,
                tooltipY + 48,
                0xEE101B2A
        );

        graphics.drawString(
                font,
                "Первый удар",
                tooltipX + 6,
                tooltipY + 5,
                0xFFFFFFFF
        );

        graphics.drawString(
                font,
                "Урон первой атаки выше",
                tooltipX + 6,
                tooltipY + 18,
                0xFFD5E5EC
        );

        graphics.drawString(
                font,
                "Стоимость: 10 душ",
                tooltipX + 6,
                tooltipY + 32,
                0xFF65E8DF
        );
    }
}

@Override
public boolean isPauseScreen() {
    return false;
}
