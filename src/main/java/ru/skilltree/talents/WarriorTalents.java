package ru.skilltree.talents;

import java.util.List;

public class WarriorTalents {

    public static final Talent FIRST_STRIKE = new Talent(
            "warrior_first_strike",
            "warrior",
            "Первый удар",
            "Увеличивает урон первой атаки по противнику.",
            10,
            List.of(),
            List.of()
    );

    public static final Talent HEAVY_BLOW = new Talent(
            "warrior_heavy_blow",
            "warrior",
            "Тяжёлый удар",
            "Усиливает атаки тяжёлым оружием.",
            20,
            List.of("warrior_first_strike"),
            List.of()
    );

    public static final Talent COMBAT_TRAINING = new Talent(
            "warrior_combat_training",
            "warrior",
            "Боевая подготовка",
            "Повышает эффективность ближнего боя.",
            25,
            List.of("warrior_first_strike"),
            List.of()
    );

    public static final Talent STRONG_GRIP = new Talent(
            "warrior_strong_grip",
            "warrior",
            "Крепкая хватка",
            "Даёт дополнительное преимущество при использовании оружия.",
            30,
            List.of("warrior_heavy_blow"),
            List.of()
    );

    public static final Talent BATTLE_INSTINCT = new Talent(
            "warrior_battle_instinct",
            "warrior",
            "Боевой инстинкт",
            "Открывает следующий этап развития воина.",
            35,
            List.of("warrior_combat_training"),
            List.of()
    );

    public static List<Talent> getAll() {
        return List.of(
                FIRST_STRIKE,
                HEAVY_BLOW,
                COMBAT_TRAINING,
                STRONG_GRIP,
                BATTLE_INSTINCT
        );
    }
}
