
package ru.skilltree.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ru.skilltree.SkillTreeMod;

public record SoulSyncPacket(int souls) implements CustomPacketPayload {

    public static final Type<SoulSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    SkillTreeMod.MOD_ID,
                    "soul_sync"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, SoulSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    SoulSyncPacket::souls,
                    SoulSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SoulSyncPacket packet, IPayloadContext context) {
        context.player().setData(
                ru.skilltree.attachment.ModAttachments.SOULS.get(),
                packet.souls()
        );
    }
}
