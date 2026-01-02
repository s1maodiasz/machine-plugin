package com.github.s1maodyasz.machine.item;

import com.github.s1maodyasz.machine.model.TextConfiguration;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.google.gson.Gson;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class ItemFactory<Configuration, Data> {

  protected @NotNull NamespacedKey key;
  protected @NotNull CustomItemProvider provider;
  protected @NotNull Gson gson;

  public abstract ItemStack create(@NotNull Configuration configuration, @NotNull Data data);

  String apply(
      @NotNull TextConfiguration text, @NotNull Configuration configuration, @NotNull Data data) {
    var s = text.content();
    if (s.isEmpty()) return "";
    final var placeholders = text.placeholders();
    if (placeholders.isEmpty()) return s;
    for (final var raw : placeholders) {
      if (raw == null || raw.isBlank()) continue;
      final var id = raw.trim().toLowerCase(Locale.ROOT);
      final var token = "{" + id + "}";
      s = s.replace(token, resolve(id, configuration, data));
    }
    return s;
  }

  abstract String resolve(String placeholder, Configuration configuration, Data data);
}
