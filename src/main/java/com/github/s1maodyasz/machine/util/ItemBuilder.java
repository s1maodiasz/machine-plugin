package com.github.s1maodyasz.machine.util;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemBuilder {

  public static final ItemStack NONE = ItemBuilder.of(Material.STONE).build();

  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  private ItemStack item;

  @Getter(lazy = true)
  private final ItemMeta meta =
      Objects.requireNonNull(item.getItemMeta(), "ItemMeta null for " + item.getType());

  public static @NotNull ItemBuilder of() {
    return of(Material.AIR);
  }

  public static @NotNull ItemBuilder of(@NotNull Material material) {
    return of(material, 1);
  }

  public static @NotNull ItemBuilder of(@NotNull Material material, int amount) {
    return new ItemBuilder(new ItemStack(material, Math.max(1, amount)));
  }

  public static @NotNull ItemBuilder of(@NotNull ItemStack base) {
    return new ItemBuilder(base.clone());
  }

  public @NotNull ItemBuilder type(Material type) {
    this.item.setType(type);
    return this;
  }

  public @NotNull ItemBuilder type(String typeName) {
    final var material = MaterialsUtil.valueOf(typeName);
    return type(material);
  }

  public @NotNull ItemBuilder stack(ItemStack item) {
    this.item = item;
    this.meta = item.getItemMeta();
    return this;
  }

  public @NotNull ItemBuilder amount(int amount) {
    item.setAmount(Math.max(1, amount));
    return this;
  }

  public @NotNull ItemBuilder name(@Nullable String miniMsg) {
    meta().displayName(miniMsg == null ? null : MINI_MESSAGE.deserialize(miniMsg));
    return this;
  }

  public @NotNull ItemBuilder name(@Nullable Component component) {
    meta().displayName(component);
    return this;
  }

  public @NotNull ItemBuilder lore(@Nullable List<String> linesMiniMsg) {
    if (linesMiniMsg == null) {
      meta().lore(null);
      return this;
    }
    List<Component> lore = new ArrayList<>(linesMiniMsg.size());
    for (String s : linesMiniMsg) lore.add(MINI_MESSAGE.deserialize(s));
    meta().lore(lore);
    return this;
  }

  public @NotNull ItemBuilder unbreakable(boolean unbreakable) {
    meta().setUnbreakable(unbreakable);
    return this;
  }

  public @NotNull ItemBuilder flags(@NotNull ItemFlag... flags) {
    meta().addItemFlags(flags);
    return this;
  }

  public @NotNull ItemBuilder hideAllFlags() {
    meta().addItemFlags(ItemFlag.values());
    return this;
  }

  public @NotNull ItemBuilder removeFlags(@NotNull ItemFlag... flags) {
    meta().removeItemFlags(flags);
    return this;
  }

  public @NotNull ItemBuilder enchant(@NotNull Enchantment enchantment, int level, boolean unsafe) {
    meta().addEnchant(enchantment, level, unsafe);
    return this;
  }

  public @NotNull ItemBuilder unenchant(@NotNull Enchantment enchantment) {
    meta().removeEnchant(enchantment);
    return this;
  }

  public @NotNull ItemBuilder glow(boolean glow) {
    if (glow) {
      meta().addEnchant(Enchantment.UNBREAKING, 1, true);
      meta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
    } else {
      meta().removeEnchant(Enchantment.UNBREAKING);
    }
    return this;
  }

  public @NotNull ItemBuilder damage(int damage) {
    if (meta() instanceof Damageable d) d.setDamage(Math.max(0, damage));
    return this;
  }

  public @NotNull ItemBuilder attribute(
      @NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
    meta().addAttributeModifier(attribute, modifier);
    return this;
  }

  public @NotNull ItemBuilder clearAttributes() {
    meta().setAttributeModifiers(null);
    return this;
  }

  public @NotNull ItemBuilder leatherColor(@NotNull Color color) {
    if (meta() instanceof LeatherArmorMeta lam) lam.setColor(color);
    return this;
  }

  public @NotNull ItemBuilder potionColor(@NotNull Color color) {
    if (meta() instanceof PotionMeta pm) pm.setColor(color);
    return this;
  }

  public @NotNull ItemBuilder skullOwner(@NotNull UUID uuid, @NotNull String name) {
    if (meta() instanceof SkullMeta sm) {
      PlayerProfile profile = Bukkit.createProfile(uuid, name);
      sm.setOwnerProfile(profile);
    }
    return this;
  }

  public @NotNull ItemBuilder skullTextureUrl(@NotNull String textureUrl) {
    if (!(meta() instanceof SkullMeta sm)) return this;
    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "head");
    PlayerTextures textures = profile.getTextures();
    try {
      textures.setSkin(new URL(textureUrl));
    } catch (Exception ignored) {
      return this;
    }
    profile.setTextures(textures);
    sm.setOwnerProfile(profile);
    return this;
  }

  public @NotNull ItemBuilder banner(@NotNull Consumer<BannerMeta> editor) {
    if (meta() instanceof BannerMeta bm) editor.accept(bm);
    return this;
  }

  public @NotNull ItemBuilder book(@NotNull Consumer<BookMeta> editor) {
    if (meta() instanceof BookMeta bm) editor.accept(bm);
    return this;
  }

  public @NotNull ItemBuilder firework(@NotNull Consumer<FireworkMeta> editor) {
    if (meta() instanceof FireworkMeta fm) editor.accept(fm);
    return this;
  }

  public @NotNull ItemBuilder meta(@NotNull Consumer<ItemMeta> editor) {
    editor.accept(meta());
    return this;
  }

  public @NotNull Pdc pdc() {
    return new Pdc(this);
  }

  public @NotNull ItemStack build() {
    item.setItemMeta(meta());
    return item;
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @Accessors(fluent = true)
  public static final class Pdc {
    private final ItemBuilder builder;

    private PersistentDataContainer container() {
      return builder.meta().getPersistentDataContainer();
    }

    public <T, Z> @NotNull ItemBuilder set(
        @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
      container().set(key, type, value);
      return builder;
    }

    public @NotNull ItemBuilder string(@NotNull NamespacedKey key, @NotNull String value) {
      container().set(key, PersistentDataType.STRING, value);
      return builder;
    }

    public @NotNull ItemBuilder integer(@NotNull NamespacedKey key, int value) {
      container().set(key, PersistentDataType.INTEGER, value);
      return builder;
    }

    public @NotNull ItemBuilder longs(@NotNull NamespacedKey key, long value) {
      container().set(key, PersistentDataType.LONG, value);
      return builder;
    }

    public @NotNull ItemBuilder bool(@NotNull NamespacedKey key, boolean value) {
      container().set(key, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
      return builder;
    }

    public @NotNull ItemBuilder remove(@NotNull NamespacedKey key) {
      container().remove(key);
      return builder;
    }

    public @NotNull ItemBuilder edit(@NotNull Consumer<PersistentDataContainer> consumer) {
      consumer.accept(container());
      return builder;
    }
  }
}
