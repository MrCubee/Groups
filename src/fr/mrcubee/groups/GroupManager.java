package fr.mrcubee.groups;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrCubee
 * @since 1.0
 */
public class GroupManager {

    private final Map<String, Group> groups;
    private String defaultGroupName;

    public GroupManager() {
        this.groups = new HashMap<String, Group>();
    }

    public Group getGroup(String name) {
        if (name == null)
            return null;
        return this.groups.get(name);
    }

    protected boolean registerGroup(Group group) {
        if (group == null || group.name == null || this.groups.containsKey(group.name))
            return false;
        this.groups.put(group.name, group);
        return true;
    }

    protected boolean unRegisterGroup(String name) {
        Group group;

        if (name == null)
            return false;
        group = this.groups.remove(name);
        return group != null;
    }

    public String getDefaultGroupName() {
        return this.defaultGroupName;
    }

    public Group getDefaultGroup() {
        return getGroup(this.defaultGroupName);
    }

    public void setDefaultGroup(String groupName) {
        this.defaultGroupName = groupName;
    }

    protected void removeAllGroups() {
        this.groups.clear();
        this.defaultGroupName = null;
    }
}
