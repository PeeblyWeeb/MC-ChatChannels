package com.salamithecat.chatChannels.Listeners;

import com.salamithecat.chatChannels.Constants;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class DiscordSRVListener {
    @Subscribe
    public void preMessageProcess(GameChatMessagePreProcessEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (dataContainer.has(Constants.ChannelKey)) {
            event.setCancelled(true);
        }
    }
}
