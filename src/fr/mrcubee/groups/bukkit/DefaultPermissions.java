package fr.mrcubee.groups.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

/**
 * @author MrCubee
 * @since 1.0
 */
public class DefaultPermissions {

    public static void clearDefaultPermissions() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        Bukkit.getLogger().info("Disable NoOp permissions:");
        pluginManager.getDefaultPermissions(false).forEach(perm -> {
            System.out.println("- " + perm.getName());
            perm.setDefault(PermissionDefault.FALSE);
        });
    }
}
