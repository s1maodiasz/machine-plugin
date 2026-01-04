package com.github.s1maodyasz.machine.provider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MiniMessageProvider {

    public static final MiniMessage MM = MiniMessage.miniMessage();
}
