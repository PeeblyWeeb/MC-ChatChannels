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
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class ChannelListParticipantsCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("list_participants")
                .executes(ChannelListParticipantsCommand::Execute);
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
            boolean currentHiddenValue = Optional.ofNullable(dataContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);

            Component participants_str = Component.join(
                    JoinConfiguration.separator(Component.text(", ", NamedTextColor.GRAY)),
                    channel.Participants.stream().map(CommandSender::name).toList()
            );

            player.sendRichMessage(
                    "<gray>There are <participant_count> participants in the channel </gray><channel_name><gray>:</gray>\n\n<participants>",
                    Placeholder.component("participant_count", Component.text(channel.Participants.size())),
                    Placeholder.component("channel_name", Component.text(currentHiddenValue ? "[hidden]" : channel.Name)),
                    Placeholder.component("participants", participants_str)
            );
        } else {
            player.sendRichMessage("<red>You aren't in a channel!</red>");
        }

        return Command.SINGLE_SUCCESS;
    }
}
