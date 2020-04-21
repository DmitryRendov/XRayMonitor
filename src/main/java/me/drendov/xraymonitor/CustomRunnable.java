package me.drendov.xraymonitor;

import org.bukkit.command.CommandSender;

public class CustomRunnable
        implements Runnable {
    CommandSender sender;
    String world;
    String oreName;
    String bantype;
    float maxrate;
    boolean banned;
    int hours;

    CustomRunnable(CommandSender sender, String world, String oreName, String bantype, float maxrate, boolean banned, int hours) {
        this.sender = sender;
        this.hours = hours;
        this.world = world;
        this.oreName = oreName;
        this.bantype = bantype;
        this.maxrate = maxrate;
        this.banned = banned;
    }

    @Override
    public void run() {
    }
}

