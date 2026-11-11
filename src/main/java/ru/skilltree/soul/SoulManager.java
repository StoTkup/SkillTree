package ru.skilltree.soul;

import net.minecraft.world.entity.player.Player;
import ru.skilltree.attachment.ModAttachments;

public class SoulManager {

    public static int getSouls(Player player) {
        return player.getData(ModAttachments.SOULS.get());
    }

    public static void addSouls(Player player, int amount) {
        if (amount <= 0) return;

        int current = getSouls(player);
        player.setData(ModAttachments.SOULS.get(), current + amount);
    }

    public static boolean removeSouls(Player player, int amount) {
        if (amount <= 0) return false;

        int current = getSouls(player);

        if (current < amount) {
            return false;
        }

        player.setData(ModAttachments.SOULS.get(), current - amount);
        return true;
    }
}
