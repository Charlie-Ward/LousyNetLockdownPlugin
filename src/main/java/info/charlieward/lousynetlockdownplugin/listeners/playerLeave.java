package info.charlieward.lousynetlockdownplugin.listeners;

import info.charlieward.lousynetlockdownplugin.LousyNetLockdownPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.Objects;

public class playerLeave implements Listener {
    static LousyNetLockdownPlugin plugin;
    public playerLeave(LousyNetLockdownPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public static void playerLeaves(PlayerKickEvent event) {
        String lockdownState = plugin.jedis.get("lockdownState");
        if(lockdownState.equals("true")) {
            event.setLeaveMessage(null);
        }

    }


}
