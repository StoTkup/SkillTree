
package ru.skilltree.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import ru.skilltree.SkillTreeMod;

@EventBusSubscriber(
        modid = SkillTreeMod.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class SoulHud {

    private static final int SOUL_COLOR = 0xFF20A6A0;
    private static final int BACKGROUND_COLOR = 0xAA082D2D;

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(
                        SkillTreeMod.MOD_ID,
                        "souls_hud"
                ),
                SoulHud::render
        );
    }

    private static void render(GuiGraphics graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        int souls = minecraft.player.getData(
                ru.skilltree.attachment.ModAttachments.SOULS.get()
        );

        int x = graphics.guiWidth() - 100;
        int y = graphics.guiHeight() - 45;

        graphics.fill(
                x - 6, y - 5,
                x + 94, y + 17,
                BACKGROUND_COLOR
        );

        graphics.drawString(
                minecraft.font,
                "◆ Души: " + souls,
                x,
                y,
                SOUL_COLOR,
                true
        );
    }
}
