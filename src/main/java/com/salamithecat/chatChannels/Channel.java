package com.salamithecat.chatChannels;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    public static HashMap<String, Channel> Registry = new HashMap<>();
    public Set<Player> Participants = ConcurrentHashMap.newKeySet();

    public String Name;

    public Channel(String name) {
        Name = name;
    }

    public static Channel get(String name) {
        if (Registry.containsKey(name))
            return Registry.get(name);

        // create channel if it doesn't already exist
        Channel channel = new Channel(name);
        Registry.put(name, channel);

        return channel;
    }

    public static void dispose(String name) {
        Registry.remove(name);
    }

    public void addParticipant(Player player) {
        for (Player participant : Participants) {
            PersistentDataContainer participantContainer = participant.getPersistentDataContainer();
            boolean participantHiddenValue = Optional.ofNullable(participantContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);

            participant.sendRichMessage(
                "<gray>[#<channelname>]</gray> <playername> joined the channel",
                Placeholder.component("channelname", Component.text(participantHiddenValue ? "" : Name)),
                Placeholder.component("playername", player.name())
            );
        }
        Participants.add(player);

        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        boolean currentHiddenValue = Optional.ofNullable(dataContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);

        dataContainer.set(Constants.ChannelKey, PersistentDataType.STRING, Name);
        player.sendRichMessage("<gray>You have joined the</gray> #" + (currentHiddenValue ? "[hidden]" : Name) + " <gray>channel</gray>");
    }

    public void removeParticipant(Player player, boolean silent, boolean persistent) {
        Participants.remove(player);
        for (Player participant : Participants) {
            PersistentDataContainer participantContainer = participant.getPersistentDataContainer();
            boolean participantHiddenValue = Optional.ofNullable(participantContainer.get(Constants.ChannelHiddenKey, PersistentDataType.BOOLEAN)).orElse(false);

            participant.sendRichMessage(
                    "<gray>[#<channelname>]</gray> <playername> left the channel",
                    Placeholder.component("channelname", Component.text(participantHiddenValue ? "" : Name)),
                    Placeholder.component("playername", player.name())
            );
        }

        if (!silent)
            player.sendRichMessage("<gray>You are now in the</gray> #global <gray>channel</gray>");
        if (persistent)
            player.getPersistentDataContainer().remove(Constants.ChannelKey);

        if (Participants.isEmpty()) {
            Channel.dispose(Name);
        }
    }
}
