package fr.mrcubee.groups.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author MrCubee
 * @since 1.0
 */
public class PlayerChangeNameEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String oldName;

    public PlayerChangeNameEvent(Player player, String oldName) {
        this.player = player;
        this.oldName = oldName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getOldName() {
        return this.oldName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
