package info.charlieward.lousynetlockdownplugin.commands;

import info.charlieward.lousynetlockdownplugin.LousyNetLockdownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class lockdownToggle implements CommandExecutor {
    static LousyNetLockdownPlugin plugin;
    public lockdownToggle(LousyNetLockdownPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("LousyNetLockdownPlugin.admin")) {
                String lockdownState = plugin.jedis.get("lockdownState");
                if (lockdownState.equals("false")) {
                    player.sendMessage(ChatColor.BLUE + "[LousyNet-Lockdown]" + ChatColor.WHITE + "Enabling lockdown");
                    plugin.jedis.set("lockdownState", "true");
                    String playerName = player.getDisplayName();
                    plugin.jedis.set("lockdownPlayer", playerName);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String date = dtf.format(now);
                    plugin.jedis.set("lockdownTimeStart", date);

                    plugin.getPlayerList(player);

                } else if (lockdownState.equals("true")) {
                    player.sendMessage(ChatColor.BLUE + "[LousyNet-Lockdown] " + ChatColor.WHITE + "Disabling lockdown");
                    plugin.jedis.set("lockdownState", "false");
                    plugin.jedis.set("lockdownEndPlayer", player.getDisplayName());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String date = dtf.format(now);
                    plugin.jedis.set("lockdownTimeEnd", date);
                }
            } else {
                player.sendMessage(ChatColor.BLUE + "[LousyNet] " + ChatColor.RED + "You do not have the correct permissions to run this command");
            }
        } else {
            String lockdownState = plugin.jedis.get("lockdownState");
            if(lockdownState.equals("false")) {
                System.out.println("[LousyNet-Lockdown] Enabling lockdown");
                plugin.jedis.set("lockdownState", "true");
                plugin.jedis.set("lockdownPlayer", "Console");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf.format(now);
                plugin.jedis.set("lockdownTimeStart", date);
            } else if (lockdownState.equals("true")) {
                System.out.println("[LousyNet-Lockdown] Disabling lockdown");
                plugin.jedis.set("lockdownState", "false");
                plugin.jedis.set("lockdownEndPlayer", "Console");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf.format(now);
                plugin.jedis.set("lockdownTimeEnd", date);
            }
        }
        return true;
    }
}
