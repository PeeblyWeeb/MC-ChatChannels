package com.salamithecat.chatChannels.Listeners;

import com.salamithecat.chatChannels.ChatChannels;
import com.salamithecat.chatChannels.Constants;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class DiscordSRVListener {
    public static boolean Ready = false;

    @Subscribe
    public void onReady(DiscordReadyEvent event) {
        Ready = true;
    }

    @Subscribe
    public void preMessageProcess(GameChatMessagePreProcessEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(Constants.ChannelKey) && !ChatChannels.ignoredPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
        ChatChannels.ignoredPlayers.remove(player.getUniqueId());
    }
}
