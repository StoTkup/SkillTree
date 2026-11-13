package ru.skilltree.talents;

import java.util.ArrayList;
import java.util.List;

public class TalentRegistry {

    private static final List<Talent> TALENTS = new ArrayList<>();

    static {
        TALENTS.addAll(WarriorTalents.getAll());
    }

    public static List<Talent> getAll() {
        return List.copyOf(TALENTS);
    }

    public static Talent getById(String id) {
        return TALENTS.stream()
                .filter(talent -> talent.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
