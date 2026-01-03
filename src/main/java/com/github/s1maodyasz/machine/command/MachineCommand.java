package com.github.s1maodyasz.machine.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.s1maodyasz.machine.issuer.IssueResult;
import com.github.s1maodyasz.machine.issuer.Issuer;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("maquina|maquinas|machine|machines")
@RequiredArgsConstructor
public final class MachineCommand extends BaseCommand {

    private static final String ADMIN_PERMISSION = "machine.admin";

    private final Plugin plugin;
    private final Issuer<MachineConfiguration, MachineData> issuer;

    @Default
    public void onDefault(CommandSender sender) {}

    @Subcommand("issue")
    @Description("Dá máquinas a um jogador")
    @Syntax("<player> <key> <amount>")
    @CommandCompletion("@players @key 1|5|10|64")
    public void onIssue(CommandSender sender, String playerName, String key, double amount) {
        final ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");

        final String prefix = messages != null ? messages.getString(MessageConstants.PREFIX, "") : "";

        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            final String msg = messages != null ? messages.getString(MessageConstants.NO_PERMISSION, "") : "";
            MessageBuilder.of(prefix + msg)
                    .with(MessageConstants.PLACEHOLDER_PERMISSION, ADMIN_PERMISSION)
                    .send(sender);
            return;
        }

        if (amount <= 0) {
            final String msg = messages != null ? messages.getString(MessageConstants.ISSUE_INVALID_AMOUNT, "") : "";
            MessageBuilder.of(prefix + msg)
                    .with(MessageConstants.PLACEHOLDER_AMOUNT, amount)
                    .send(sender);
            return;
        }

        final Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) return;

        final MachineData data = MachineData.builder().stack(amount).build();
        final IssueResult result = issuer.issue(target, key, data);

        final String path =
                switch (result) {
                    case SUCCESS -> MessageConstants.ISSUE_SUCCESS;
                    case INVALID_KEY -> MessageConstants.ISSUE_INVALID_KEY;
                    case INVALID_AMOUNT -> MessageConstants.ISSUE_INVALID_AMOUNT;
                    case INVENTORY_FULL -> MessageConstants.ISSUE_INVENTORY_FULL;
                };

        final String msg = messages != null ? messages.getString(path, "") : "";
        MessageBuilder.of(prefix + msg)
                .with(MessageConstants.PLACEHOLDER_PLAYER, playerName)
                .with(MessageConstants.PLACEHOLDER_KEY, key)
                .with(MessageConstants.PLACEHOLDER_AMOUNT, amount)
                .send(sender);
    }
}
