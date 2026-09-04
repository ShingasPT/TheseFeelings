package me.shingas.theseFeelings.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    /**
     * Sets a cooldown
     *
     * @param player  Player
     * @param key     Unique cooldown key (e.g. "heal", "teleport", "mycommand")
     * @param seconds Cooldown length in seconds
     */

    public void set(Player player, String key, int seconds) {
        cooldowns
                .computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .put(key, System.currentTimeMillis() + (seconds * 1000L));
    }

    /**
     * Checks if a cooldown is active
     */
    public boolean isActive(Player player, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return false;

        Long endTime = playerCooldowns.get(key);
        if (endTime == null) return false;

        // Cleanup expired cooldowns automatically
        if (endTime <= System.currentTimeMillis()) {
            playerCooldowns.remove(key);
            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(player.getUniqueId());
            }
            return false;
        }

        return true;
    }

    /**
     * Gets remaining cooldown time in seconds
     */
    public long getRemaining(Player player, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return 0;

        Long endTime = playerCooldowns.get(key);
        if (endTime == null) return 0;

        long remaining = endTime - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

}
