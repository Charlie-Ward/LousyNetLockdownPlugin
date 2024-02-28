package info.charlieward.lousynetlockdownplugin.listeners;

import info.charlieward.lousynetlockdownplugin.LousyNetLockdownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    static LousyNetLockdownPlugin plugin;
    public playerJoin(LousyNetLockdownPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public static void playerJoins(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String lockdownState = plugin.jedis.get("lockdownState");
        if (lockdownState.equals("true")) {
            if(player.hasPermission("LousyNetLockdownPlugin.admin")) {
                player.sendMessage(ChatColor.RED + "Network is currently in lockdown mode");
                player.sendMessage(ChatColor.WHITE + "Enabled by: " + ChatColor.GRAY + plugin.jedis.get("lockdownPlayer"));
                player.sendMessage(ChatColor.WHITE + "Enabled at: " + ChatColor.GRAY + plugin.jedis.get("lockdownTimeStart"));
            } else {
                event.setJoinMessage(null);
                player.kickPlayer("This server is currently in lockdown mode. Only staff members are allowed to join at this moment. Please stand by for more information");
            }
        }
    }

}
