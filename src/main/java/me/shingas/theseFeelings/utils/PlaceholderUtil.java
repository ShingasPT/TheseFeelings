package me.shingas.theseFeelings.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public class PlaceholderUtil {

    // We keep a cheap “is PAPI available?” check.
    // Using Supplier lets you pass plugin::isPapiEnabled if you already track it.
    private static Supplier<Boolean> papiAvailable = () ->
            Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    private PlaceholderUtil() {}

    /** Optional: call once in onEnable() to use your plugin's cached flag. */
    public static void init(Plugin plugin, Supplier<Boolean> availabilitySupplier) {
        papiAvailable = availabilitySupplier != null ? availabilitySupplier : papiAvailable;
    }

    public static boolean isVanished(Player player) {
        if (!papiAvailable.get() || player == null) return false;

        String result = PlaceholderAPI.setPlaceholders(
                player,
                "%simplevanish_is-vanished%"
        );

        return Boolean.parseBoolean(result);
    }

}
