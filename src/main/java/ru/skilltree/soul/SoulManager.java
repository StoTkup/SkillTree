
package ru.skilltree.soul;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import ru.skilltree.attachment.ModAttachments;
import ru.skilltree.network.SoulSyncPacket;

public class SoulManager {

    public static int getSouls(Player player) {
        return player.getData(ModAttachments.SOULS.get());
    }

    public static void addSouls(Player player, int amount) {
        if (amount <= 0) return;

        int current = getSouls(player);
        int updated = current + amount;

        player.setData(ModAttachments.SOULS.get(), updated);

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new SoulSyncPacket(updated)
            );
        }
    }

    public static boolean removeSouls(Player player, int amount) {
        if (amount <= 0) return false;

        int current = getSouls(player);

        if (current < amount) {
            return false;
        }

        int updated = current - amount;
        player.setData(ModAttachments.SOULS.get(), updated);

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new SoulSyncPacket(updated)
            );
        }

        return true;
    }
}
