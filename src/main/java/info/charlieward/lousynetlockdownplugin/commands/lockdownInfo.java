package info.charlieward.lousynetlockdownplugin.commands;

import info.charlieward.lousynetlockdownplugin.LousyNetLockdownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class lockdownInfo implements CommandExecutor {

    LousyNetLockdownPlugin plugin;
    public lockdownInfo(LousyNetLockdownPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("LousyNetLockdownPlugin.admin")) {
                String lockdownStatus = plugin.jedis.get("lockdownState");
                String lockdownOwner = plugin.jedis.get("lockdownPlayer");
                String lockdownStopPlayer = plugin.jedis.get("lockdownEndPlayer");
                String lockdownTimeStart = plugin.jedis.get("lockdownTimeStart");
                String lockdownTimeEnd = plugin.jedis.get("lockdownTimeEnd");

                player.sendMessage(ChatColor.BLUE + "------Lockdown Info------");
                if (lockdownStatus.equals("false")) {
                    player.sendMessage(ChatColor.WHITE + "Lockdown Not Currently Active");
                    if (lockdownOwner == null) {
                        player.sendMessage(ChatColor.WHITE + "Lockdown never ran");
                    } else {
                        player.sendMessage(ChatColor.WHITE + "Last Lockdown Info");
                        player.sendMessage(ChatColor.WHITE + "Started by: " + ChatColor.GRAY + lockdownOwner);
                        player.sendMessage(ChatColor.WHITE + "Ended by: " + ChatColor.GRAY + lockdownStopPlayer);
                        player.sendMessage(ChatColor.WHITE + "Started at: " + ChatColor.GRAY + lockdownTimeStart);
                        player.sendMessage(ChatColor.WHITE + "Ended at: " + ChatColor.GRAY + lockdownTimeEnd);
                    }
                } else {
                    player.sendMessage(ChatColor.WHITE + "Lockdown Active");
                    player.sendMessage(ChatColor.WHITE + "Started by: " + ChatColor.GRAY + lockdownOwner);
                    player.setDisplayName(ChatColor.WHITE + "Started at: " + ChatColor.GRAY + lockdownTimeStart);
                }
                player.sendMessage(ChatColor.BLUE + "-------------------------");
            } else {
                player.sendMessage(ChatColor.BLUE + "[LousyNet] " + ChatColor.RED + "You do not have the correct permissions to run this command");
            }
        } else {
            String lockdownStatus = plugin.jedis.get("lockdownState");
            String lockdownOwner = plugin.jedis.get("lockdownPlayer");
            String lockdownStopPlayer = plugin.jedis.get("lockdownEndPlayer");
            String lockdownTimeStart = plugin.jedis.get("lockdownTimeStart");
            String lockdownTimeEnd = plugin.jedis.get("lockdownTimeEnd");

            System.out.println("------Lockdown Info------");

            if(lockdownStatus.equals("false")) {
                System.out.println("Lockdown Not Currently Active");
                if(lockdownOwner == null) {
                    System.out.println("Lockdown never ran");
                } else {
                    System.out.println("Last Lockdown Info");
                    System.out.println("Started at: " + lockdownOwner);
                    System.out.println("Ended by: " + lockdownStopPlayer);
                    System.out.println("Started at: " + lockdownTimeStart);
                    System.out.println("Ended at: " + lockdownTimeEnd);
                }
            } else {
                System.out.println("Lockdown Active");
                System.out.println("Started by: " + lockdownOwner);
                System.out.println("Started at: " + lockdownTimeStart);
            }

            System.out.println("-------------------------");

        }

        return true;
    }
}
