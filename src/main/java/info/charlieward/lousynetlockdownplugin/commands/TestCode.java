package info.charlieward.lousynetlockdownplugin.commands;

import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TestCode {

    public static Jedis jedis = new Jedis();
    public static void main(String[] args) {
        jedis.set("lockdownState", "false");
        jedis.set("lockdownPlayer", "");
        jedis.set("lockdownEndPlayer", "");
        jedis.set("lockdownTimeStart", "");
        jedis.set("lockdownTimeEnd", "");

        setUp();
        playerJoin(true);
        playerJoin(false);
        lockdownToggle();
        playerJoin(true);
        playerJoin(false);
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

    public static void getStatus() {

        // Goes in the get status command

        System.out.println("!! Get Status Ran !!");

        String lockdownStatus = jedis.get("lockdownState");
        String lockdownOwner = jedis.get("lockdownPlayer");
        String lockdownStopPlayer = jedis.get("lockdownEndPlayer");
        String lockdownTimeStart = jedis.get("lockdownTimeStart");
        String lockdownTimeEnd = jedis.get("lockdownTimeEnd");

        if(lockdownStatus.equals("false")) {
            System.out.println("Lockdown not currently active");
            if (lockdownOwner == null) {
                System.out.println("Lockdown never run");
            } else {
                System.out.println("Last lockdown started by: " + lockdownOwner);
                System.out.println("Ended by: " + lockdownStopPlayer);
                System.out.println("Started at: " + lockdownTimeStart);
                System.out.println("Ended at: " + lockdownTimeEnd);
            }
        } else if (lockdownStatus.equals("true")) {
            System.out.println("Lockdown Currently Active");
            System.out.println("Started by: " + lockdownOwner);
            System.out.println("At: " + lockdownTimeStart);
        }
    }

    public static void lockdownToggle() {

        // Goes in the toggle command

        System.out.println("!! Toogle Ran !!");
        String lockdownStatus = jedis.get("lockdownState");
        if(lockdownStatus.equals("false")) {
            System.out.println("Setting lockdown to true");
            jedis.set("lockdownState", "true");
            String playerName = "LousyBoi";
            jedis.set("lockdownPlayer", playerName);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            jedis.set("lockdownTimeStart", date);
            // Kick all players with message to join back if staff
        } else if (lockdownStatus.equals("true")) {
            System.out.println("Setting lockdown to false");
            jedis.set("lockdownState", "false");
            String playerName = "D4rthMonkey";
            jedis.set("lockdownEndPlayer", playerName);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            jedis.set("lockdownTimeEnd", date);
        }
    }

    public static void playerJoin(Boolean allowed) {

        //Inside join event

        System.out.println("!! Player join ran !!");

        String lockdownStatus = jedis.get("lockdownState");
        if (lockdownStatus.equals("true")) {
            if(allowed) {
                System.out.println("Server is currently in lockdown");
                System.out.println("Started by: " + jedis.get("lockdownPlayer"));
                System.out.println("At: " + jedis.get("lockdownTimeStart"));
            } else {
                System.out.println("Player not allowed");
                //Cancel the join message and kick with reason
            }

        }
    }
}
