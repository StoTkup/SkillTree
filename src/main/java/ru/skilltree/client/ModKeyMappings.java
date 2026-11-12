
package ru.skilltree.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import ru.skilltree.SkillTreeMod;

@EventBusSubscriber(
        modid = SkillTreeMod.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = net.neoforged.api.distmarker.Dist.CLIENT
)
public class ModKeyMappings {

    public static final KeyMapping OPEN_SKILL_TREE =
            new KeyMapping(
                    "key.skilltree.open",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "key.categories.skilltree"
            );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_SKILL_TREE);
    }
}
