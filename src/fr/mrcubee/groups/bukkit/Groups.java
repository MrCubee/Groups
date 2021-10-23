package fr.mrcubee.groups.bukkit;

import fr.mrcubee.groups.bukkit.commands.GroupCommand;
import fr.mrcubee.groups.bukkit.listeners.RegisterListeners;
import fr.mrcubee.groups.sql.SQLDataBase;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrCubee
 * @since 1.0
 */
public class Groups extends JavaPlugin implements Listener {

    private GroupManager groupManager;
    private SQLDataBase dataBase;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        this.groupManager = new GroupManager(this);
        loadGroups();
        loadDataBase();
    }

    @Override
    public void onEnable() {
        PluginCommand pluginCommand;
        GroupCommand groupCommand = new GroupCommand(this);

        loadPlayersGroup();
        pluginCommand = getCommand("groups");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(groupCommand);
            pluginCommand.setTabCompleter(groupCommand);
        }
        RegisterListeners.register(this);
        getServer().getScheduler().runTaskLater(this, DefaultPermissions::clearDefaultPermissions, 20L);
    }

    @Override
    public void onDisable() {
        this.groupManager.removeAllPlayerGroups();
    }

    public void loadGroups() {
        this.groupManager.removeAllPlayerGroups();
        this.groupManager.loadGroupsFromConfig(getConfig());
        this.groupManager.setDefaultGroup("default");
    }

    public void loadDataBase() {
        ConfigurationSection sqlSection = getConfig().getConfigurationSection("database");

        if (this.dataBase != null) {
            this.dataBase.disconnect();
            this.dataBase = null;
        }
        this.dataBase = SQLDataBase.create(sqlSection.getString("host"),
                sqlSection.getString("database"),
                sqlSection.getString("table"),
                sqlSection.getString("user"),
                sqlSection.getString("password"));
        getLogger().warning("Database: " + this.dataBase);
    }

    public void loadPlayersGroup() {
        for (Player player : Bukkit.getOnlinePlayers())
            this.groupManager.setPlayerGroup(player, this.getDataBase().getPlayerGroup(player.getUniqueId()));
    }

    public void reload() {
        saveDefaultConfig();
        loadGroups();
        loadDataBase();
        loadPlayersGroup();
    }

    public GroupManager getGroupManager() {
        return this.groupManager;
    }

    public SQLDataBase getDataBase() {
        return this.dataBase;
    }
}

