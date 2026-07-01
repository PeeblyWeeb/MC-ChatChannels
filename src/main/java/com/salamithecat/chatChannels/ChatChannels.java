package com.salamithecat.chatChannels;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.salamithecat.chatChannels.Listeners.ChannelEventListener;
import com.salamithecat.chatChannels.Listeners.DiscordSRVListener;
import github.scarsz.discordsrv.DiscordSRV;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatChannels extends JavaPlugin {

    @Override
    public void onEnable() {
        LiteralCommandNode<CommandSourceStack> channelCommand = Commands.literal("channel")
                .then(Commands.literal("join")
                        .then(Commands.argument("name", StringArgumentType.word())
                            .executes(ctx -> {
                                String channelName = StringArgumentType.getString(ctx, "name");

                                CommandSender sender = ctx.getSource().getSender();
                                Entity executor = ctx.getSource().getExecutor();

                                if (!(executor instanceof Player player)) {
                                    sender.sendPlainMessage("Only players can change their chat channel.");
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
                            })))
                .then(Commands.literal("leave")
                        .executes(ctx -> {

                            CommandSender sender = ctx.getSource().getSender();
                            Entity executor = ctx.getSource().getExecutor();

                            if (!(executor instanceof Player player)) {
                                sender.sendPlainMessage("Only players can change their chat channel.");
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
                        }))
                .build();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(channelCommand);
        });

        getServer().getPluginManager().registerEvents(new ChannelEventListener(), this);

        // enable DiscordSRV integration
        Plugin discordSRV = getServer().getPluginManager().getPlugin("DiscordSRV");
        if (discordSRV != null && discordSRV.isEnabled()) {
            this.getLogger().info("DiscordSRV is available; hooking into GameChatMessagePreProcessEvent");
            DiscordSRV.api.subscribe(new DiscordSRVListener());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
