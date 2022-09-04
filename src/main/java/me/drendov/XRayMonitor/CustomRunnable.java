package me.drendov.XRayMonitor;

import org.bukkit.command.CommandSender;

public class CustomRunnable
        implements Runnable {
    CommandSender sender;
    String world;
    String oreName;
    float rate;
    int hours;

    CustomRunnable(CommandSender sender, String world, String oreName, float rate, int hours) {
        this.sender = sender;
        this.hours = hours;
        this.world = world;
        this.oreName = oreName;
        this.rate = rate;
    }

    @Override
    public void run() {
    }
}

