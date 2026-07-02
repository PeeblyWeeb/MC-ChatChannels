package com.salamithecat.chatChannels.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.salamithecat.chatChannels.Channel;
import com.salamithecat.chatChannels.Constants;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChannelLeaveCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("leave")
                .executes(ChannelLeaveCommand::Execute);
    }

    private static int Execute(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Non-player executor is not permitted to execute this command.");
            return Command.SINGLE_SUCCESS;
        }

        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(Constants.ChannelKey)) {
            Channel channel = Channel.get(dataContainer.get(Constants.ChannelKey, PersistentDataType.STRING));
            channel.removeParticipant(player, false, true);
        } else {
            player.sendRichMessage("<red>You aren't in a channel!</red>");
        }

        return Command.SINGLE_SUCCESS;
    }
}
