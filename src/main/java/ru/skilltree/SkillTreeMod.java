package ru.skilltree;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import ru.skilltree.attachment.ModAttachments;

@Mod(SkillTreeMod.MOD_ID)
public class SkillTreeMod {
    public static final String MOD_ID = "skilltree";

    public SkillTreeMod(IEventBus modEventBus) {
        ModAttachments.register(modEventBus);
    }
}
