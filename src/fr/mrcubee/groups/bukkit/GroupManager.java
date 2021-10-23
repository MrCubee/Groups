package fr.mrcubee.groups.bukkit;

import fr.mrcubee.groups.Group;
import fr.mrcubee.groups.bukkit.events.PlayerGroupChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

/**
 * @author MrCubee
 * @since 1.0
 */
public class GroupManager extends fr.mrcubee.groups.GroupManager {

    private final Groups groups;
    private final Map<Player, Group> playerGroup;
    private final Map<Player, PermissionAttachment> permissionAttachments;

    protected GroupManager(Groups groups) {
        this.groups = groups;
        this.playerGroup = new WeakHashMap<Player, Group>();
        this.permissionAttachments = new WeakHashMap<Player, PermissionAttachment>();
    }

    public Group getPlayerGroup(Player player) {
        Group group;

        if (player == null)
            return null;
        group = this.playerGroup.get(player);
        if (group == null)
            return getDefaultGroup();
        return group;
    }

    public Group loadGroup(ConfigurationSection configurationSection, String groupName) {
        Group result;
        List<String> permissions;
        List<String> inheritance;

        if (configurationSection == null || groupName == null || getGroup(groupName) != null)
            return null;
        permissions = configurationSection.getStringList("permissions");
        inheritance = configurationSection.getStringList("inheritance");
        result = new Group(this, groupName, new HashSet<String>(permissions), new HashSet<String>(inheritance));
        if (registerGroup(result))
            return result;
        return null;
    }

    public void loadGroupsFromConfig(FileConfiguration fileConfiguration) {
        ConfigurationSection configurationSection;

        if (fileConfiguration == null)
            return;
        removeAllGroups();
        configurationSection = fileConfiguration.getConfigurationSection("groups");
        if (configurationSection == null)
            return;
        for (String key : configurationSection.getKeys(false))
            loadGroup(configurationSection.getConfigurationSection(key), key);
        setDefaultGroup("default");
    }

    public boolean setPlayerGroup(Player player, String groupName) {
        Bukkit.getLogger().info("Setting " + player.getName() + " to group " + groupName);
        Group originGroup;
        Group group;
        Set<String> permissions;
        PermissionAttachment permissionAttachment;
        PlayerGroupChangeEvent playerGroupChangeEvent;

        if (player == null)
            return false;
        originGroup = this.playerGroup.get(player);
        if (groupName == null)
            group = getDefaultGroup();
        else {
            group = getGroup(groupName);
            if (group == null)
                group = getDefaultGroup();
        }
        if (originGroup != null && originGroup.equals(group))
            return true;
        playerGroupChangeEvent = new PlayerGroupChangeEvent(player, originGroup, group);
        Bukkit.getPluginManager().callEvent(playerGroupChangeEvent);
        if (playerGroupChangeEvent.isCancelled())
            return false;
        permissionAttachment = this.permissionAttachments.remove(player);
        if (permissionAttachment != null)
            permissionAttachment.remove();
        if (group == null) {
            this.playerGroup.remove(player);
            return true;
        }
        permissions = group.getPermissions();
        if (permissions != null && !permissions.isEmpty()) {
            permissionAttachment = player.addAttachment(this.groups);
            for (String permission : group.getPermissions())
                permissionAttachment.setPermission(permission, true);
            this.permissionAttachments.put(player, permissionAttachment);
        }
        if (group.equals(getDefaultGroup()))
            this.playerGroup.remove(player);
        else
            this.playerGroup.put(player, group);
        return true;
    }

    public boolean setPlayerGroup(Player player, Group group) {
        if (player == null)
            return false;
        return setPlayerGroup(player, group != null ? group.name : null);
    }

    public void removeAllPlayerGroups() {
        this.playerGroup.clear();
        for (PermissionAttachment permissionAttachment : this.permissionAttachments.values())
            permissionAttachment.remove();
        this.permissionAttachments.clear();
    }
}
