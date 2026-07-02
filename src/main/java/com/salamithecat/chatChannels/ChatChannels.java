package com.salamithecat.chatChannels;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.salamithecat.chatChannels.Commands.ChannelIncognitoCommand;
import com.salamithecat.chatChannels.Commands.ChannelJoinCommand;
import com.salamithecat.chatChannels.Commands.ChannelLeaveCommand;
import com.salamithecat.chatChannels.Commands.ChannelListParticipantsCommand;
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
                .then(ChannelJoinCommand.createCommand())
                .then(ChannelLeaveCommand.createCommand())
                .then(ChannelListParticipantsCommand.createCommand())
                .then(ChannelIncognitoCommand.createCommand())
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
