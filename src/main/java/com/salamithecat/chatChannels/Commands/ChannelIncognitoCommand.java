package com.salamithecat.chatChannels.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.salamithecat.chatChannels.Channel;
import com.salamithecat.chatChannels.Constants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class ChannelIncognitoCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("incognito")
                .executes(ChannelIncognitoCommand::Execute);
    }

    private static int Execute(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Non-player executor is not permitted to execute this command.");
            return Command.SINGLE_SUCCESS;
        }

        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        boolean currentHiddenValue = Optional.ofNullable(dataContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);
        dataContainer.set(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN, !currentHiddenValue);

        if (!currentHiddenValue) {
            player.sendRichMessage("<gray>Your current channel name will now be</gray> hidden<gray>.");
        } else {
            player.sendRichMessage("<gray>Your current channel name will now be</gray> visible<gray>.");
        }

        return Command.SINGLE_SUCCESS;
    }
}
