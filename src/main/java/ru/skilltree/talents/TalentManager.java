package ru.skilltree.talents;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import ru.skilltree.attachment.ModAttachments;
import ru.skilltree.soul.SoulManager;

import java.util.ArrayList;
import java.util.List;

public class TalentManager {

    public static boolean canLearn(ServerPlayer player, Talent talent) {
        if (talent == null) {
            return false;
        }

        // Проверяем души
        if (SoulManager.getSouls(player) < talent.soulCost()) {
            return false;
        }

        // Проверяем необходимые таланты
        List<String> learned =
                player.getData(ModAttachments.LEARNED_TALENTS.get());

        if (!learned.containsAll(talent.requiredTalents())) {
            return false;
        }

        // Проверяем необходимые предметы
        for (String itemId : talent.requiredItems()) {
            boolean found = false;

            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack stack = player.getInventory().getItem(slot);

                if (!stack.isEmpty()
                        && net.minecraft.core.registries.BuiltInRegistries.ITEM
                        .getKey(stack.getItem()).toString().equals(itemId)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    public static boolean learn(ServerPlayer player, String talentId) {
        Talent talent = TalentRegistry.getById(talentId);

        if (!canLearn(player, talent)) {
            return false;
        }

        List<String> learned = new ArrayList<>(
                player.getData(ModAttachments.LEARNED_TALENTS.get())
        );

        if (learned.contains(talentId)) {
            return false;
        }

        if (!SoulManager.removeSouls(player, talent.soulCost())) {
            return false;
        }

        // Списываем необходимые предметы
        for (String itemId : talent.requiredItems()) {
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack stack = player.getInventory().getItem(slot);

                if (!stack.isEmpty()
                        && net.minecraft.core.registries.BuiltInRegistries.ITEM
                        .getKey(stack.getItem()).toString().equals(itemId)) {
                    stack.shrink(1);
                    break;
                }
            }
        }

        learned.add(talentId);

        player.setData(
                ModAttachments.LEARNED_TALENTS.get(),
                List.copyOf(learned)
        );

        return true;
    }
}
