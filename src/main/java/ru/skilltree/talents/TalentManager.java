
package ru.skilltree.talents;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

import ru.skilltree.attachment.ModAttachments;
import ru.skilltree.soul.SoulManager;

import java.util.ArrayList;
import java.util.List;

public class TalentManager {

    public static boolean canLearn(ServerPlayer player, Talent talent) {
        if (talent == null) {
            return false;
        }

        List<String> learned =
                player.getData(ModAttachments.LEARNED_TALENTS.get());

        if (learned.contains(talent.id())) {
            return false;
        }

        if (SoulManager.getSouls(player) < talent.soulCost()) {
            return false;
        }

        if (!learned.containsAll(talent.requiredTalents())) {
            return false;
        }

        // Проверяем, хватает ли предметов с учётом повторений
        List<String> remainingItems =
                new ArrayList<>(talent.requiredItems());

        for (int slot = 0;
             slot < player.getInventory().getContainerSize();
             slot++) {

            ItemStack stack = player.getInventory().getItem(slot);

            if (stack.isEmpty()) {
                continue;
            }

            String itemId = BuiltInRegistries.ITEM
                    .getKey(stack.getItem()).toString();

            for (int i = remainingItems.size() - 1; i >= 0; i--) {
                if (remainingItems.get(i).equals(itemId)) {
                    remainingItems.remove(i);
                }
            }
        }

        return remainingItems.isEmpty();
    }

    public static boolean learn(ServerPlayer player, String talentId) {
        Talent talent = TalentRegistry.getById(talentId);

        if (!canLearn(player, talent)) {
            return false;
        }

        List<String> learned = new ArrayList<>(
                player.getData(ModAttachments.LEARNED_TALENTS.get())
        );

        if (!SoulManager.removeSouls(player, talent.soulCost())) {
            return false;
        }

        // Списываем необходимые предметы
        for (String itemId : talent.requiredItems()) {
            for (int slot = 0;
                 slot < player.getInventory().getContainerSize();
                 slot++) {

                ItemStack stack = player.getInventory().getItem(slot);

                if (!stack.isEmpty()
                        && BuiltInRegistries.ITEM.getKey(stack.getItem())
                        .toString().equals(itemId)) {

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
