package fr.mrcubee.groups.bukkit.listeners;

import fr.mrcubee.groups.bukkit.Groups;

/**
 * @author MrCubee
 * @since 1.0
 */
public class RegisterListeners {

    public static void register(Groups plugin) {
        if (plugin == null)
            return;
        fr.mrcubee.groups.bukkit.listeners.player.RegisterListeners.register(plugin);
    }
}
