package info.charlieward.lousynetlockdownplugin;

import info.charlieward.lousynetlockdownplugin.commands.lockdownInfo;
import info.charlieward.lousynetlockdownplugin.listeners.playerJoin;
import info.charlieward.lousynetlockdownplugin.listeners.playerLeave;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.Objects;

public final class LousyNetLockdownPlugin extends JavaPlugin {

    private static LousyNetLockdownPlugin plugin;

    public Jedis jedis = new Jedis();

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("LousyNet-LockdownPlugin v." + this.getDescription().getVersion() + " has loaded.");
        String loadState = jedis.get("lockdownState");
        if ( (loadState == null || loadState.isEmpty()) || (!loadState.equals("true") && !loadState.equals("false")) ) {
            getLogger().info("[LousyNet-LockdownPlugin] Lockdown variable not set or set to invalid value. Setting to false");
            jedis.set("lockdownState", "false");
        } else if (loadState.equals("true")) {
            getLogger().info("[LousyNet-LockdownPlugin] A lockdown is currently active on the network.");
        } else if (loadState.equals("false")) {
            getLogger().info("[LousyNet-LockdownPlugin] No lockdown is currently active on the network");
        }

        getCommand("lockdownInfo").setExecutor(new lockdownInfo(this));

        getServer().getPluginManager().registerEvents(new playerJoin(this), this);
        getServer().getPluginManager().registerEvents(new playerLeave(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LousyNet-LockdownPlugin v." + this.getDescription().getVersion() + " has been disabled.");
    }
    public static LousyNetLockdownPlugin getPlugin() {
        return plugin;
    }
}
