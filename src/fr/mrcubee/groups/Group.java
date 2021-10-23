package fr.mrcubee.groups;

import java.util.*;

/**
 * @author MrCubee
 * @since 1.0
 */
public class Group {

    private final GroupManager groupManager;

    public final String name;
    protected final Set<String> permissions;
    protected final Set<String> inheritances;

    public Group(GroupManager groupManager, String name) {
        this.groupManager = groupManager;
        this.name = name;
        this.permissions = new HashSet<String>();
        this.inheritances = new HashSet<String>();
    }

    public Group(GroupManager groupManager, String name, Set<String> permissions, Set<String> inheritance) {
        this.groupManager = groupManager;
        this.name = name;
        this.permissions = new HashSet<String>(permissions);
        this.inheritances = new HashSet<String>(inheritance);
    }

    public String getName() {
        return this.name;
    }

    public boolean addPermission(String permission) {
        return permission != null && this.permissions.add(permission);
    }

    public boolean removePermission(String permission) {
        return permission != null && this.permissions.remove(permission);
    }

    public boolean hasPermissionInGroup(String permission) {
        return permission != null && this.permissions.contains(permission);
    }

    public boolean hasPermission(String permission) {
        Group group;

        if (permission == null)
            return false;
        else if (this.permissions.contains(permission))
            return true;
        for (String inheritance : this.inheritances) {
            group = this.groupManager.getGroup(inheritance);
            if (group != null && group.hasPermission(permission))
                return true;
        }
        return false;
    }

    public Set<String> getGroupPermissions() {
        return new HashSet<String>(this.permissions);
    }

    public Set<String> getPermissions() {
        Set<String> result = new HashSet<String>(this.permissions);
        Group group;

        for (String inheritance : this.inheritances) {
            group = this.groupManager.getGroup(inheritance);
            if (group != null)
                result.addAll(group.getPermissions());
        }
        return result;
    }

    public boolean addInheritance(String groupName) {
        if (groupName == null)
            return false;
        return this.inheritances.add(groupName);
    }

    public boolean removeInheritance(String groupName) {
        if (groupName == null)
            return false;
        return this.inheritances.remove(groupName);
    }

    public boolean containInheritance(String groupName) {
        return groupName != null && this.inheritances.contains(groupName);
    }

    public Set<String> getInheritances() {
        return new HashSet<String>(this.inheritances);
    }

    public Group clone(String name) {
        if (name == null)
            return null;
        return new Group(this.groupManager, name, this.permissions, this.inheritances);
    }

    public Group clone() {
        return clone(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }
}
