package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageSender {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static void send(@NotNull Audience audience, @NotNull String message, TagResolver... resolvers) {
        if (message.isBlank()) return;
        audience.sendMessage(MINI_MESSAGE.deserialize(message, resolvers));
    }

    public static @NotNull TagResolver placeholder(@NotNull String key, @NotNull String value) {
        return Placeholder.parsed(key, value);
    }

    public static @NotNull MiniMessage miniMessage() {
        return MINI_MESSAGE;
    }
}