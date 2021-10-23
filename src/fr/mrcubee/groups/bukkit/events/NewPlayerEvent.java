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
public class NewPlayerEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public NewPlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
