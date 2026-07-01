package com.salamithecat.chatChannels;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Constants {
    public static NamespacedKey ChannelKey = new NamespacedKey(JavaPlugin.getPlugin(ChatChannels.class), "current_channel");
}
