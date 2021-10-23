package fr.mrcubee.groups.bukkit.events;

import fr.mrcubee.groups.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author MrCubee
 * @since 1.0
 */
public class PlayerGroupChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Group oldGroup;
    private final Group newGroup;
    private boolean cancelled;

    public PlayerGroupChangeEvent(Player player, Group oldGroup, Group newGroup) {
        this.player = player;
        this.oldGroup = oldGroup;
        this.newGroup = newGroup;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Group getOldGroup() {
        return this.oldGroup;
    }

    public Group getNewGroup() {
        return this.newGroup;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
