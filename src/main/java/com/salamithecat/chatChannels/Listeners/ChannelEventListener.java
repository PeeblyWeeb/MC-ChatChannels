package com.salamithecat.chatChannels.Listeners;

import com.salamithecat.chatChannels.Channel;
import com.salamithecat.chatChannels.Constants;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class ChannelEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(Constants.ChannelKey)) {
            Channel channel = Channel.get(dataContainer.get(Constants.ChannelKey, PersistentDataType.STRING));
            channel.addParticipant(player);
        }
    }

    public void HandleDisconnectedPlayer(Player player) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(Constants.ChannelKey)) {
            Channel channel = Channel.get(dataContainer.get(Constants.ChannelKey, PersistentDataType.STRING));
            channel.removeParticipant(player, false, false);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HandleDisconnectedPlayer(player);
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
        Player player = event.getPlayer();
        HandleDisconnectedPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessageSent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(Constants.ChannelKey)) {
            Channel channel = Channel.get(dataContainer.get(Constants.ChannelKey, PersistentDataType.STRING));

            event.viewers().clear();
            event.viewers().addAll(channel.Participants);

            ChatRenderer previousRenderer = event.renderer();
            event.renderer(((source, sourceDisplayName, message, viewer) -> {
                boolean currentHiddenValue = false;

                if (viewer instanceof Player renderPlayer) {
                    PersistentDataContainer renderDataContainer = renderPlayer.getPersistentDataContainer();
                    currentHiddenValue = Optional.ofNullable(renderDataContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);
                }

                Component rendered = previousRenderer.render(source, sourceDisplayName, message, viewer);
                return Component.text("")
                        .append(Component.text("[#" + (currentHiddenValue ? "" : channel.Name) + "] ", NamedTextColor.GRAY))
                        .append(rendered);
            }));
        }
    }
}
