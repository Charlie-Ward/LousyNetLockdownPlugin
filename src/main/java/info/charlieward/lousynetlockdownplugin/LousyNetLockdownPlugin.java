package info.charlieward.lousynetlockdownplugin;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import info.charlieward.lousynetlockdownplugin.commands.lockdownInfo;
import info.charlieward.lousynetlockdownplugin.commands.lockdownToggle;
import info.charlieward.lousynetlockdownplugin.listeners.playerJoin;
import info.charlieward.lousynetlockdownplugin.listeners.playerLeave;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public final class LousyNetLockdownPlugin extends JavaPlugin implements PluginMessageListener {

    private static LousyNetLockdownPlugin plugin;

    public Jedis jedis = new Jedis();

    public String[] playerList;


    @Override
    public void onEnable() {
        plugin = this;

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

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
        getCommand("lockdown").setExecutor(new lockdownToggle(this));

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


    public void getPlayerList(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if(!channel.equals("BungeeCord")) { return; }

        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String subchannel = in.readUTF();
        if(!subchannel.equals("PlayerList")) {
            String server = in.readUTF();
            this.playerList = in.readUTF().split(", ");
            System.out.println(Arrays.toString(playerList));
        }

    }
}
