package com.github.s1maodyasz.machine.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineSerializeData;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("maquina|maquinas|machine|machines")
@RequiredArgsConstructor
public final class MachineCommand extends BaseCommand {

    private static final String ADMIN_PERMISSION = "machine.admin";

    private final Plugin plugin;
    private final AbstractItemIssuer<MachineConfiguration, MachineSerializeData> issuer;

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
            System.out.println(MessageConstants.UNAUTHORIZED);
            sender.sendMessage(
                MiniMessageProvider.MM.deserialize(MessageConstants.UNAUTHORIZED, TagResolver.builder().resolver(Placeholder.parsed("player", playerName)).build()));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(MiniMessageProvider.MM.deserialize(MessageConstants.ISSUE_INVALID_AMOUNT, TagResolver.builder().resolver(Placeholder.parsed("amount", String.valueOf(amount))).build()));
            return;
        }
        final Player target = Bukkit.getPlayerExact(playerName);

        if (target == null) return;

        final var data = MachineSerializeData.builder().key(key).build();

        final IssueResult result = issuer.issue(target, data, amount);
        switch (result) {
            case SUCCESS:
                sender.sendMessage(MiniMessageProvider.MM.deserialize(MessageConstants.ISSUE_SUCCESS, TagResolver.builder().resolver(Placeholder.parsed("player", playerName)).resolver(Placeholder.parsed("key", key)).resolver(Placeholder.parsed("amount", String.valueOf(amount))).build()));
                break;
            case INVALID_KEY:
                sender.sendMessage(MessageConstants.ISSUE_INVALID_KEY);
                break;
            case INVALID_AMOUNT:
                sender.sendMessage(MessageConstants.ISSUE_INVALID_AMOUNT);
                break;
            case INVENTORY_FULL:
                sender.sendMessage(MessageConstants.ISSUE_INVENTORY_FULL);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }
    }
}
