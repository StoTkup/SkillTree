
package ru.skilltree.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import ru.skilltree.SkillTreeMod;

@EventBusSubscriber(
        modid = SkillTreeMod.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD
)
public class ModNetworking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {

        var registrar = event.registrar("1");

        // Синхронизация душ с клиентом
        registrar.playToClient(
                SoulSyncPacket.TYPE,
                SoulSyncPacket.STREAM_CODEC,
                SoulSyncPacket::handle
        );

        // Синхронизация изученных талантов
        registrar.playToClient(
                TalentSyncPacket.TYPE,
                TalentSyncPacket.STREAM_CODEC,
                TalentSyncPacket::handle
        );

        // Запрос клиента на изучение таланта
        registrar.playToServer(
                LearnTalentPacket.TYPE,
                LearnTalentPacket.STREAM_CODEC,
                LearnTalentPacket::handle
        );
    }
}
