
package ru.skilltree.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import ru.skilltree.SkillTreeMod;
import ru.skilltree.attachment.ModAttachments;

import java.util.List;

@EventBusSubscriber(modid = SkillTreeMod.MOD_ID)
public class SoulLoginSync {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            // Синхронизация количества душ
            int souls = player.getData(ModAttachments.SOULS.get());

            PacketDistributor.sendToPlayer(
                    player,
                    new SoulSyncPacket(souls)
            );

            // Синхронизация изученных талантов
            List<String> talents =
                    player.getData(ModAttachments.LEARNED_TALENTS.get());

            PacketDistributor.sendToPlayer(
                    player,
                    new TalentSyncPacket(talents)
            );
        }
    }
}
