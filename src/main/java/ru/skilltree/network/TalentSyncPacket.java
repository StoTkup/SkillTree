
package ru.skilltree.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import ru.skilltree.SkillTreeMod;
import ru.skilltree.attachment.ModAttachments;

import java.util.List;

public record TalentSyncPacket(List<String> talents) implements CustomPacketPayload {

    public static final Type<TalentSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    SkillTreeMod.MOD_ID,
                    "talent_sync"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, TalentSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
                    TalentSyncPacket::talents,
                    TalentSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TalentSyncPacket packet, IPayloadContext context) {
        context.player().setData(
                ModAttachments.LEARNED_TALENTS.get(),
                List.copyOf(packet.talents())
        );
    }
}
