package fr.mrcubee.groups.bukkit.commands;

import fr.mrcubee.groups.Group;
import fr.mrcubee.groups.bukkit.GroupManager;
import fr.mrcubee.groups.bukkit.Groups;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author MrCubee
 * @since 1.0
 */
public class GroupCommand implements CommandExecutor, TabCompleter {

    private final Groups groups;
    private final GroupManager groupManager;

    public GroupCommand(Groups groups) {
        this.groups = groups;
        this.groupManager = groups.getGroupManager();
    }

    public boolean onCommandRemove(CommandSender sender, String[] args) {
        Player target;

        if (args.length < 2)
            return false;
        else if (!sender.hasPermission("groups.command.set")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found.");
            return true;
        }
        if (this.groupManager.setPlayerGroup(target, (String) null))
            sender.sendMessage(ChatColor.GREEN + "The role has been assigned to the player.");
        else
            sender.sendMessage(ChatColor.RED + "An error occurred while assigning the role.");
        this.groups.getDataBase().setPlayerGroup(target.getUniqueId(), null);
        return true;
    }

    public boolean onCommandSet(CommandSender sender, String[] args) {
        Player target;

        if (args.length < 3)
            return false;
        else if (!sender.hasPermission("groups.command.set")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found.");
            return true;
        }
        if (this.groupManager.setPlayerGroup(target, args[2]))
            sender.sendMessage(ChatColor.GREEN + "The role has been assigned to the player.");
        else
            sender.sendMessage(ChatColor.RED + "An error occurred while assigning the role.");
        this.groups.getDataBase().setPlayerGroup(target.getUniqueId(), args[2]);
        return true;
    }

    public boolean onCommandGet(CommandSender sender, String[] args) {
        Player target;
        Group group;

        if (args.length < 2)
            return false;
        else if (!sender.hasPermission("groups.command.set") && !sender.hasPermission("groups.command.get")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found.");
            return true;
        }
        group = this.groupManager.getPlayerGroup(target);
        if (group == null)
            sender.sendMessage(ChatColor.GOLD + "The player does not have a group.");
        else
            sender.sendMessage(ChatColor.GREEN + "His group is: " + group.name);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        switch (args[0].toLowerCase()) {
            case "set":
                return onCommandSet(sender, args);
            case "get":
                return onCommandGet(sender, args);
            case "remove":
                return onCommandRemove(sender, args);
            case "reload":
                sender.sendMessage(ChatColor.GREEN + "Reloading...");
                this.groups.reload();
                sender.sendMessage(ChatColor.GREEN + "Groups reloaded !");
                return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> result = new LinkedList<String>();

        if (args.length == 1) {
            if (commandSender.hasPermission("groups.command.set"))
                result.addAll(Arrays.asList("set", "remove"));
            if (commandSender.hasPermission("groups.command.get"))
                result.add("get");
            result.removeIf(value -> !value.toLowerCase().startsWith(args[0].toLowerCase()));
        } else if (args.length == 2) {
            Bukkit.getOnlinePlayers().forEach(player -> result.add(player.getName()));
            result.removeIf(value -> !value.toLowerCase().startsWith(args[1].toLowerCase()));
        }
        return result;
    }
}
