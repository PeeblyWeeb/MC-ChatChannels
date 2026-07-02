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

public class ChannelJoinCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("join")
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(ChannelJoinCommand::Execute)
                );
    }

    private static int Execute(CommandContext<CommandSourceStack> ctx) {
        String channelName = StringArgumentType.getString(ctx, "name");

        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Non-player executor is not permitted to execute this command.");
            return Command.SINGLE_SUCCESS;
        }

        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(Constants.ChannelKey)) {
            Channel channel = Channel.get(dataContainer.get(Constants.ChannelKey, PersistentDataType.STRING));
            channel.removeParticipant(player, true, true);
        }

        Channel targetChannel = Channel.get(channelName);
        targetChannel.addParticipant(player);

        return Command.SINGLE_SUCCESS;
    }
}
