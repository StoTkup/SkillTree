
package ru.skilltree.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import ru.skilltree.SkillTreeMod;
import ru.skilltree.attachment.ModAttachments;
import ru.skilltree.talents.TalentManager;

import java.util.List;

public record LearnTalentPacket(String talentId)
        implements CustomPacketPayload {

    public static final Type<LearnTalentPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    SkillTreeMod.MOD_ID,
                    "learn_talent"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, LearnTalentPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    LearnTalentPacket::talentId,
                    LearnTalentPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(
            LearnTalentPacket packet,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            boolean success = TalentManager.learn(
                    player,
                    packet.talentId()
            );

            if (success) {
                List<String> learned =
                        player.getData(ModAttachments.LEARNED_TALENTS.get());

                PacketDistributor.sendToPlayer(
                        player,
                        new TalentSyncPacket(learned)
                );

                player.sendSystemMessage(
                        Component.literal("Талант успешно изучен!")
                );
            } else {
                player.sendSystemMessage(
                        Component.literal(
                                "Не удалось изучить талант: проверь требования."
                        )
                );
            }
        });
    }
}
