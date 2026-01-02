package com.github.s1maodyasz.machine.message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageBuilder {

    private static final Pattern HEX = Pattern.compile("(?i)&?#([0-9a-f]{6})");
    private static final Pattern PH = Pattern.compile("\\{([a-zA-Z0-9_.-]+)}");

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
        .character('&')
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .build();

    private final List<String> lines;
    private final Map<String, Object> placeholders = new HashMap<>();

    public static MessageBuilder of(String text) {
        return new MessageBuilder(List.of(safe(text)));
    }

    public static MessageBuilder of(List<String> lines) {
        var safe = safeLines(lines);
        return new MessageBuilder(safe.isEmpty() ? List.of("") : safe);
    }

    public MessageBuilder with(String key, Object value) {
        if (key == null || key.isEmpty()) return this;
        placeholders.put(key, value);
        return this;
    }

    public MessageBuilder withAll(Map<String, ?> map) {
        if (map == null || map.isEmpty()) return this;
        placeholders.putAll(map);
        return this;
    }

    public String build() {
        return format(lines.getFirst(), placeholders);
    }

    public List<String> buildList() {
        var out = new ArrayList<String>(lines.size());
        for (var line : lines) out.add(format(line, placeholders));
        return out;
    }

    public Component buildComponent() {
        return toComponent(lines.getFirst(), placeholders);
    }

    public List<Component> buildComponentList() {
        var out = new ArrayList<Component>(lines.size());
        for (var line : lines) out.add(toComponent(line, placeholders));
        return out;
    }

    public void send(CommandSender sender) {
        if (sender == null) return;
        for (var c : buildComponentList()) sender.sendMessage(c);
    }

    public static String format(String text, Map<String, ?> placeholders) {
        var s = applyPlaceholders(text, placeholders);
        return color(s);
    }

    public static String color(String text) {
        var s = safe(text);
        s = applyHex(s);
        return LEGACY.serialize(LEGACY.deserialize(s));
    }

    public static Component toComponent(String text, Map<String, ?> placeholders) {
        var s = applyPlaceholders(text, placeholders);
        s = applyHex(safe(s));
        return LEGACY.deserialize(s);
    }

    public static String applyPlaceholders(String text, Map<String, ?> placeholders) {
        var safe = safe(text);
        if (placeholders == null || placeholders.isEmpty()) return safe;

        var m = PH.matcher(safe);
        var out = new StringBuilder();

        while (m.find()) {
            var key = m.group(1);
            var val = placeholders.get(key);
            var rep = val == null ? m.group(0) : String.valueOf(val);
            m.appendReplacement(out, Matcher.quoteReplacement(rep));
        }

        m.appendTail(out);
        return out.toString();
    }

    private static String applyHex(String text) {
        var m = HEX.matcher(text);
        var out = new StringBuilder();

        while (m.find()) {
            var hex = m.group(1);
            var rep = toLegacyHex(hex);
            m.appendReplacement(out, Matcher.quoteReplacement(rep));
        }

        m.appendTail(out);
        return out.toString();
    }

    private static String toLegacyHex(String rgb) {
        var h = rgb.toLowerCase(Locale.ROOT);
        var sb = new StringBuilder("§x");
        for (var c : h.toCharArray()) sb.append('§').append(c);
        return sb.toString();
    }

    private static List<String> safeLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) return List.of();
        var out = new ArrayList<String>(lines.size());
        for (var s : lines) out.add(safe(s));
        return out;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}