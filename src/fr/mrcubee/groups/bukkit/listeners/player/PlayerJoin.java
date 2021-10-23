package fr.mrcubee.groups.bukkit.listeners.player;

import fr.mrcubee.groups.bukkit.GroupManager;
import fr.mrcubee.groups.bukkit.Groups;
import fr.mrcubee.groups.bukkit.events.NewPlayerEvent;
import fr.mrcubee.groups.bukkit.events.PlayerChangeNameEvent;
import fr.mrcubee.groups.sql.SQLDataBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author MrCubee
 * @since 1.0
 */
public class PlayerJoin implements Listener {

    private final Groups groups;
    private final GroupManager groupManager;

    protected PlayerJoin(Groups groups) {
        this.groups = groups;
        this.groupManager = groups.getGroupManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SQLDataBase database = this.groups.getDataBase();
        String storedName = database.getPlayerName(event.getPlayer().getUniqueId());
        this.groupManager.setPlayerGroup(event.getPlayer(),
                database.getPlayerGroup(event.getPlayer().getUniqueId()));

        if (storedName == null) {
            groups.getServer().getPluginManager().callEvent(new NewPlayerEvent(event.getPlayer()));
            database.setPlayerName(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        } else if (!event.getPlayer().getName().equals(storedName)) {
            groups.getServer().getPluginManager().callEvent(new PlayerChangeNameEvent(event.getPlayer(), storedName));
            database.setPlayerName(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        }
    }
}
