package info.charlieward.lousynetlockdownplugin;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.Objects;

public final class LousyNetLockdownPlugin extends JavaPlugin {

    public Jedis jedis = new Jedis();

    @Override
    public void onEnable() {
        getLogger().info("LousyNet-LockdownPlugin v." + this.getDescription().getVersion() + " has loaded.");
        String loadState = jedis.get("lockdownStatus");
        if (loadState != "true" || loadState != "false") {
            getLogger().info("[LousyNet-LockdownPlugin] Lockdown variable not set. Setting to false");
            jedis.set("lockdownState", "false");
        } else{
            if (Objects.equals(loadState, "true")) {
                getLogger().info("[LousyNet-LockdownPlugin] Lockdown is currently active server wide");
            } else {
                getLogger().info("[LousyNet-LockdownPlugin] Lockdown is not currently active server wide");
            }
        }


    }

    @Override
    public void onDisable() {
        getLogger().info("LousyNet-LockdownPlugin v." + this.getDescription().getVersion() + " has been disabled.");
    }
}
