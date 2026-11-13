
package ru.skilltree.attachment;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import ru.skilltree.SkillTreeMod;

import java.util.List;
import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    SkillTreeMod.MOD_ID
            );

    // Количество душ игрока
    public static final Supplier<AttachmentType<Integer>> SOULS =
            ATTACHMENTS.register("souls", () ->
                    AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT)
                            .copyOnDeath()
                            .build()
            );

    // Список изученных игроком талантов
    public static final Supplier<AttachmentType<List<String>>> LEARNED_TALENTS =
            ATTACHMENTS.register("learned_talents", () ->
                    AttachmentType.builder(() -> List.<String>of())
                            .serialize(Codec.STRING.listOf())
                            .copyOnDeath()
                            .build()
            );

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}
