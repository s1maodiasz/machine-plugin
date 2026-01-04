package com.github.s1maodyasz.machine.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.s1maodyasz.machine.issuer.IssueResult;
import com.github.s1maodyasz.machine.issuer.ItemIssuer;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.MiniMessageProvider;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("bateria|baterias|battery|batteries")
@RequiredArgsConstructor
public final class BatteryCommand extends BaseCommand {

    private static final String ADMIN_PERMISSION = "machine.admin";

    private final Plugin plugin;
    private final ItemIssuer<BatteryConfiguration, BatteryData> itemIssuer;

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage("&cNothing is here.");
    }

    @Subcommand("issue")
    @Description("Dá baterias a um jogador")
    @Syntax("<player> <key> <amount>")
    @CommandCompletion("@players @key 1|5|10|64")
    public void onIssue(CommandSender sender, String playerName, String key, int amount) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.UNAUTHORIZED,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", playerName))
                            .build()));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.ISSUE_INVALID_AMOUNT,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("amount", String.valueOf(amount)))
                            .build()));
            return;
        }
        final Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) return;
        final var data = BatteryData.builder().key(key).build();
        final IssueResult result = itemIssuer.issue(target, data, amount);
        final String path =
                switch (result) {
                    case SUCCESS -> MessageConstants.ISSUE_SUCCESS;
                    case INVALID_KEY -> MessageConstants.ISSUE_INVALID_KEY;
                    case INVALID_AMOUNT -> MessageConstants.ISSUE_INVALID_AMOUNT;
                    case INVENTORY_FULL -> MessageConstants.ISSUE_INVENTORY_FULL;
                };
        sender.sendMessage(MiniMessageProvider.MM.deserialize(
                path,
                TagResolver.builder()
                        .resolver(Placeholder.parsed("player", playerName))
                        .resolver(Placeholder.parsed("key", key))
                        .resolver(Placeholder.parsed("amount", String.valueOf(amount)))
                        .build()));
    }
}
