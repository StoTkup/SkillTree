package ru.skilltree.talents;

import java.util.List;

/**
 * Каталог всех талантов мода.
 */
public class TalentRegistry {

    private static final List<Talent> TALENTS = List.of();

    public static List<Talent> getAll() {
        return TALENTS;
    }

    public static Talent getById(String id) {
        return TALENTS.stream()
                .filter(talent -> talent.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
