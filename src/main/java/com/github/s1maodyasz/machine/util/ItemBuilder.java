package com.github.s1maodyasz.machine.util;

import com.github.s1maodyasz.machine.provider.MiniMessageProvider;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemBuilder {

    public static final ItemStack NONE = ItemBuilder.of(Material.STONE).build();

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item.clone();
        this.meta = Objects.requireNonNull(this.item.getItemMeta(), "ItemMeta null for " + this.item.getType());
    }

    public static @NotNull ItemBuilder of() {
        return of(Material.AIR);
    }

    public static @NotNull ItemBuilder of(@NotNull Material material) {
        return new ItemBuilder(new ItemStack(material, 1));
    }

    public static @NotNull ItemBuilder of(@NotNull Material material, int amount) {
        return new ItemBuilder(new ItemStack(material, Math.max(1, amount)));
    }

    public static @NotNull ItemBuilder of(@NotNull ItemStack base) {
        return new ItemBuilder(base);
    }

    public @NotNull ItemBuilder type(@NotNull Material type) {
        this.item.setType(type);
        this.meta = Objects.requireNonNull(this.item.getItemMeta(), "ItemMeta null for " + this.item.getType());
        return this;
    }

    public @NotNull ItemBuilder type(@NotNull String typeName) {
        final var material = MaterialsUtil.valueOf(typeName);
        return type(material);
    }

    public @NotNull ItemBuilder stack(@NotNull ItemStack item) {
        this.item = item.clone();
        this.meta = Objects.requireNonNull(this.item.getItemMeta(), "ItemMeta null for " + this.item.getType());
        return this;
    }

    public @NotNull ItemBuilder amount(int amount) {
        item.setAmount(Math.max(1, amount));
        return this;
    }

    public @NotNull ItemBuilder name(@Nullable String miniMsg) {
        meta().displayName(miniMsg == null ? null : MiniMessageProvider.MM.deserialize(miniMsg));
        return this;
    }

    public @NotNull ItemBuilder name(@Nullable Component component) {
        meta().displayName(component);
        return this;
    }

    public @NotNull ItemBuilder lore(@Nullable List<Component> lines) {
        if (lines == null) {
            meta().lore(null);
            return this;
        }

        // cópia defensiva (e garante mutável caso algum meta/impl mexa)
        meta().lore(new ArrayList<>(lines));
        return this;
    }

    public @NotNull ItemBuilder lore(@Nullable String... lines) {
        if (lines == null) {
            meta().lore(null);
            return this;
        }

        final List<Component> lore = new ArrayList<>(lines.length);
        for (String line : lines) {
            lore.add(line == null ? Component.empty() : MiniMessageProvider.MM.deserialize(line));
        }
        return lore(lore);
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

    public @NotNull ItemBuilder attribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
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
            var profile = Bukkit.createProfile(uuid, name);
            sm.setOwnerProfile(profile);
        }
        return this;
    }

    public @NotNull ItemBuilder skullTextureUrl(@NotNull String textureUrl) {
        if (!(meta() instanceof SkullMeta sm)) return this;

        var profile = Bukkit.createProfile(UUID.randomUUID(), "head");
        var textures = profile.getTextures();

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

    private ItemMeta meta() {
        return Objects.requireNonNull(meta, "ItemMeta is null for " + item.getType());
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
