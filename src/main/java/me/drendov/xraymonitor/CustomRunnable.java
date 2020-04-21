package me.drendov.xraymonitor;

import org.bukkit.command.CommandSender;

public class CustomRunnable
        implements Runnable {
    CommandSender sender;
    String world;
    String oreName;
    float maxrate;
    int hours;

    CustomRunnable(CommandSender sender, String world, String oreName, float maxrate, int hours) {
        this.sender = sender;
        this.hours = hours;
        this.world = world;
        this.oreName = oreName;
        this.maxrate = maxrate;
    }

    @Override
    public void run() {
    }
}

