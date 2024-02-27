package info.charlieward.lousynetlockdownplugin.commands;

import redis.clients.jedis.Jedis;

import java.util.Objects;

public class TestCode {

    public static Jedis jedis = new Jedis();
    public static void main(String[] args) {
        setUp();
    }

    public static void setUp() {

        // Goes in the onEnable of main class. Change sout to getLogger().info("msg")

        String loadState = jedis.get("lockdownState");
        if ( (loadState == null || loadState.isEmpty()) || (!loadState.equals("true") && !loadState.equals("false")) ) {
            System.out.println("[LousyNet-LockdownPlugin] Lockdown variable not set. Setting to false");
            jedis.set("lockdownState", "false");
        } else if (loadState.equals("true")) {
            System.out.println("Current active");
        } else if (loadState.equals("false")) {
            System.out.println("Not active");
        }
    }
}
