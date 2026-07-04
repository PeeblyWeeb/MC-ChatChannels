package com.salamithecat.chatChannels;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.salamithecat.chatChannels.Commands.*;
import com.salamithecat.chatChannels.Listeners.ChannelEventListener;
import com.salamithecat.chatChannels.Listeners.DiscordSRVListener;
import github.scarsz.discordsrv.DiscordSRV;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class ChatChannels extends JavaPlugin {
    public static Logger logger;
    public static Set<UUID> ignoredPlayers = ConcurrentHashMap.newKeySet();

    @Override
    public void onEnable() {
        logger = JavaPlugin.getPlugin(ChatChannels.class).getLogger();

        LiteralCommandNode<CommandSourceStack> channelCommand = Commands.literal("channel")
                .then(ChannelJoinCommand.createCommand())
                .then(ChannelLeaveCommand.createCommand())
                .then(ChannelListParticipantsCommand.createCommand())
                .then(ChannelIncognitoCommand.createCommand())
                .build();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(channelCommand);
            commands.registrar().register(GlobalChatCommand.createCommand().build());
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
