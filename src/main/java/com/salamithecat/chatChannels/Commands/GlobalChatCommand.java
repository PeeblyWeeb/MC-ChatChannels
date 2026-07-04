package com.salamithecat.chatChannels.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.salamithecat.chatChannels.ChatChannels;
import com.salamithecat.chatChannels.Listeners.DiscordSRVListener;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GlobalChatCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("g")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(GlobalChatCommand::Execute)
                );
    }

    private static int Execute(CommandContext<CommandSourceStack> ctx) {
        String msg = StringArgumentType.getString(ctx, "message");

        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Non-player executor is not permitted to execute this command.");
            return Command.SINGLE_SUCCESS;
        }

        ChatChannels.ignoredPlayers.add(player.getUniqueId());
        player.chat(msg);
        if (!DiscordSRVListener.Ready) {
            ChatChannels.ignoredPlayers.remove(player.getUniqueId());
        }

        return Command.SINGLE_SUCCESS;
    }
}
