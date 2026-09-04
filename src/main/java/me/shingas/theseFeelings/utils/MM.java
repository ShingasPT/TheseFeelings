package me.shingas.theseFeelings.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MM {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private MM() {}

    public static Component mm(String raw) {
        return MM.deserialize(raw);
    }

    public static Component mm(String raw, TagResolver... resolvers) {
        return MM.deserialize(raw, resolvers);
    }

}
