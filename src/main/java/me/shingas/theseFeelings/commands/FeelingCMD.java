package me.shingas.theseFeelings.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.shingas.theseFeelings.managers.CooldownManager;
import me.shingas.theseFeelings.utils.MM;
import me.shingas.theseFeelings.utils.PlaceholderUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeelingCMD implements BasicCommand {

    private final JavaPlugin plugin;
    private final CooldownManager cooldownManager;
    public String feeling;

    public FeelingCMD(JavaPlugin plugin, CooldownManager cooldownManager, String feeling) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.feeling = feeling;
    }

    @Override
    public void execute(CommandSourceStack ctx, String[] args) {
        CommandSender sender = ctx.getSender();
        FileConfiguration feelingsConfig = plugin.getConfig();

        String prefix = feelingsConfig.getString("Prefix");
        TagResolver tags = TagResolver.resolver(
                Placeholder.parsed("prefix", prefix)
        );

        if (args.length == 0) {
            sender.sendMessage(MM.mm("<prefix> <yellow>Usage <gray>- <green>/" + feeling + " <player>", tags));
            return;

        }
        if (args[0].equals(sender.getName())) {
            sender.sendMessage(MM.mm("<prefix> <green>You show feelings to other people, not yourself.", tags));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || PlaceholderUtil.isVanished(target)) {
            sender.sendMessage(MM.mm("<prefix> <green>Are you sure this player exists?", tags));
            return;
        }

        if (sender instanceof Player p && !p.hasPermission("vsmp.feelings.bypass")) {
            int cooldown = feelingsConfig.getInt("Cooldown");
            String cooldownMsg = feelingsConfig.getString("CooldownMsg");
            TagResolver cooldownTags = TagResolver.resolver(
                    Placeholder.parsed("time", String.valueOf(cooldownManager.getRemaining(p, "feelings"))),
                    Placeholder.parsed("prefix", prefix)
            );
            if (cooldownManager.isActive(p, "feelings")) {
                p.sendMessage(MM.mm(cooldownMsg, cooldownTags));
                return;
            }

            cooldownManager.set(p, "feelings", cooldown);
        }

        String senderMsg = feelingsConfig.getString("Feelings." + feeling + ".Sender");
        String targetMsg = feelingsConfig.getString("Feelings." + feeling + ".Target");

        tags = TagResolver.resolver(
                Placeholder.parsed("prefix", prefix),
                Placeholder.parsed("sender", sender.getName()),
                Placeholder.parsed("target", target.getName())
        );

        ConfigurationSection soundSection = feelingsConfig.getConfigurationSection("Feelings."+ feeling +".Sound");
        String soundName = soundSection.getString("Name");
        double volume = soundSection.getDouble("Volume");
        double pitch = soundSection.getDouble("Pitch");

        target.playSound(target.getLocation(), soundName, (float) volume, (float) pitch);
        sender.sendMessage(MM.mm(senderMsg, tags));
        target.sendMessage(MM.mm(targetMsg, tags));

    }

    @Override
    public Collection<String> suggest(CommandSourceStack ctx, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // What the user has currently typed (last argument)
        String current = args.length == 0 ? "" : args[args.length - 1];

        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();

            if (PlaceholderUtil.isVanished(player)) continue;

            // Show all if nothing typed, otherwise only matching names
            if (name.toLowerCase().startsWith(current.toLowerCase())) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

}
