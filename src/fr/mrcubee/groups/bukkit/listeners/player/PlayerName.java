package fr.mrcubee.groups.bukkit.listeners.player;

import fr.mrcubee.groups.bukkit.events.NewPlayerEvent;
import fr.mrcubee.groups.bukkit.events.PlayerChangeNameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerName implements Listener {

    @EventHandler
    public void onNewPlayer(NewPlayerEvent event) {
        String message = ChatColor.YELLOW + "Welcome " + ChatColor.RED + event.getPlayer().getName() + " !";

        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(message);
    }

    @EventHandler
    public void onPlayerChangeNameEvent(PlayerChangeNameEvent event) {
        String message = ChatColor.GOLD + event.getOldName() + ChatColor.YELLOW + " has changed his name, his name is now " + ChatColor.RED + event.getPlayer().getName();

        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(message);
    }
}
