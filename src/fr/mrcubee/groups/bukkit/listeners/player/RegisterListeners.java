package fr.mrcubee.groups.bukkit.listeners.player;

import fr.mrcubee.groups.bukkit.Groups;
import org.bukkit.plugin.PluginManager;

/**
 * @author MrCubee
 * @since 1.0
 */
public class RegisterListeners {

    public static void register(Groups plugin) {
        PluginManager pluginManager;

        if (plugin == null)
            return;
        pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(plugin), plugin);
        pluginManager.registerEvents(new PlayerName(), plugin);
    }
}
