
package ru.skilltree.soul;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import ru.skilltree.SkillTreeMod;

@EventBusSubscriber(
        modid = SkillTreeMod.MOD_ID,
        bus = EventBusSubscriber.Bus.GAME
)
public class MobSoulDrops {

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        LivingEntity victim = event.getEntity();
        EntityType<?> type = victim.getType();
        RandomSource random = player.getRandom();

        int souls = 0;

        // Боссы
        if (type == EntityType.ENDER_DRAGON || type == EntityType.WITHER) {
            souls = 200;
        }

        // Хранитель
        else if (type == EntityType.WARDEN) {
            souls = 50 + random.nextInt(26);
        }

        // Элитные мобы
        else if (
                type == EntityType.PIGLIN_BRUTE ||
                type == EntityType.GHAST ||
                type == EntityType.IRON_GOLEM ||
                type == EntityType.ELDER_GUARDIAN ||
                type == EntityType.EVOKER ||
                type == EntityType.RAVAGER
        ) {
            if (random.nextFloat() < 0.50F) {
                souls = 3 + random.nextInt(6);
            }
        }

        // Обычные враждебные мобы
        else if (type.getCategory() == MobCategory.MONSTER) {
            if (random.nextFloat() < 0.15F) {
                souls = 1 + random.nextInt(5);
            }
        }

        if (souls > 0) {
            SoulManager.addSouls(player, souls);
        }
    }
}
