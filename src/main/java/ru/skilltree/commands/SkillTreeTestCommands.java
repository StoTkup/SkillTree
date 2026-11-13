
package ru.skilltree.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ru.skilltree.SkillTreeMod;
import ru.skilltree.soul.SoulManager;

@EventBusSubscriber(modid = SkillTreeMod.MOD_ID)
public class SkillTreeTestCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {

        event.getDispatcher().register(
                Commands.literal("skilltree")
                        .then(Commands.literal("test")

                                // Показать количество душ
                                .then(Commands.literal("souls")
                                        .executes(context -> {
                                            var player = context.getSource()
                                                    .getPlayerOrException();

                                            int souls = SoulManager.getSouls(player);

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "У тебя " + souls + " душ."
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        })
                                )

                                // Добавить души
                                .then(Commands.literal("add")
                                        .then(Commands.argument(
                                                "amount",
                                                IntegerArgumentType.integer(1)
                                        ).executes(context -> {
                                            var player = context.getSource()
                                                    .getPlayerOrException();

                                            int amount = IntegerArgumentType.getInteger(
                                                    context, "amount"
                                            );

                                            SoulManager.addSouls(player, amount);

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "Добавлено душ: " + amount
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        }))
                                )

                                // Убрать души
                                .then(Commands.literal("remove")
                                        .then(Commands.argument(
                                                "amount",
                                                IntegerArgumentType.integer(1)
                                        ).executes(context -> {
                                            var player = context.getSource()
                                                    .getPlayerOrException();

                                            int amount = IntegerArgumentType.getInteger(
                                                    context, "amount"
                                            );

                                            if (!SoulManager.removeSouls(player, amount)) {
                                                context.getSource().sendFailure(
                                                        Component.literal(
                                                                "Недостаточно душ!"
                                                        )
                                                );
                                                return 0;
                                            }

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "Удалено душ: " + amount
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        }))
                                )

                                // Установить количество душ
                                .then(Commands.literal("set")
                                        .then(Commands.argument(
                                                "amount",
                                                IntegerArgumentType.integer(0)
                                        ).executes(context -> {
                                            var player = context.getSource()
                                                    .getPlayerOrException();

                                            int amount = IntegerArgumentType.getInteger(
                                                    context, "amount"
                                            );

                                            int current = SoulManager.getSouls(player);

                                            if (amount > current) {
                                                SoulManager.addSouls(
                                                        player, amount - current
                                                );
                                            } else if (amount < current) {
                                                SoulManager.removeSouls(
                                                        player, current - amount
                                                );
                                            }

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "Теперь у тебя душ: " + amount
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        }))
                                )

                                // Обнулить души
                                .then(Commands.literal("reset")
                                        .executes(context -> {
                                            var player = context.getSource()
                                                    .getPlayerOrException();

                                            int current = SoulManager.getSouls(player);

                                            SoulManager.removeSouls(player, current);

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(
                                                            "Количество душ обнулено."
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        })
                                )

                                // Справка
                                .then(Commands.literal("help")
                                        .executes(context -> {
                                            var source = context.getSource();

                                            source.sendSuccess(
                                                    () -> Component.literal(
                                                            "Тестовые команды SkillTree:\n"
                                                            + "/skilltree test souls\n"
                                                            + "/skilltree test add <число>\n"
                                                            + "/skilltree test remove <число>\n"
                                                            + "/skilltree test set <число>\n"
                                                            + "/skilltree test reset\n"
                                                            + "/skilltree test help"
                                                    ),
                                                    false
                                            );

                                            return 1;
                                        })
                                )
                        )
        );
    }
}
