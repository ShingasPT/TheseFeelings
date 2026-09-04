package me.shingas.theseFeelings;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.shingas.theseFeelings.commands.FeelingCMD;
import me.shingas.theseFeelings.managers.CooldownManager;
import me.shingas.theseFeelings.utils.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheseFeelings extends JavaPlugin {

    private CooldownManager cooldownManager;
    private boolean papiEnabled;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        cooldownManager = new CooldownManager();

        papiEnabled = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        PlaceholderUtil.init(this, () -> papiEnabled);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            getLogger().info("Registering feelings...");
            int count = 0;

            ConfigurationSection feelingsSection = getConfig().getConfigurationSection("Feelings");
            if (feelingsSection == null) {
                getLogger().warning("No 'Feelings' section found in config.yml.");
                return;
            }

            for (String feelingKey : feelingsSection.getKeys(false)) {
                event.registrar().register(feelingKey, new FeelingCMD(this, cooldownManager, feelingKey));
                count++;
            }

            getLogger().info("Registered " + count + " feelings.");
        });

        getLogger().info("ThoseFeelings has started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ThoseFeelings is offline.");
    }

}