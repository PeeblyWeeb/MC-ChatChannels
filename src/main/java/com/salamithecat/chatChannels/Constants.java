package com.salamithecat.chatChannels;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Constants {
    public static final NamespacedKey ChannelKey = new NamespacedKey(JavaPlugin.getPlugin(ChatChannels.class), "current_channel");
    public static final NamespacedKey ChannelHiddenKey = new NamespacedKey(JavaPlugin.getPlugin(ChatChannels.class), "channel_name_hidden");
}
