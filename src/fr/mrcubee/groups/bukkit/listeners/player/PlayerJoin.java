package fr.mrcubee.groups.bukkit.listeners.player;

import fr.mrcubee.groups.bukkit.GroupManager;
import fr.mrcubee.groups.bukkit.Groups;
import fr.mrcubee.groups.sql.SQLDataBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author MrCubee
 * @since 1.0
 */
public class PlayerJoin implements Listener {

    private final SQLDataBase database;
    private final GroupManager groupManager;

    protected PlayerJoin(Groups groups) {
        this.database = groups.getDataBase();
        this.groupManager = groups.getGroupManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.groupManager.setPlayerGroup(event.getPlayer(),
                this.database.getPlayerGroup(event.getPlayer().getUniqueId()));
    }
}
