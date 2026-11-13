
package ru.skilltree.talents;

import java.util.List;

/**
 * Описание одного таланта в Древе талантов.
 */
public record Talent(
        String id,
        String branch,
        String name,
        String description,
        int soulCost,
        List<String> requiredTalents,
        List<String> requiredItems
) {
}
