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
        event.registrar("1").playToClient(
                SoulSyncPacket.TYPE,
                SoulSyncPacket.STREAM_CODEC,
                SoulSyncPacket::handle
        );

        event.registrar("1").playToClient(
                TalentSyncPacket.TYPE,
                TalentSyncPacket.STREAM_CODEC,
                TalentSyncPacket::handle
        );
    }
}
