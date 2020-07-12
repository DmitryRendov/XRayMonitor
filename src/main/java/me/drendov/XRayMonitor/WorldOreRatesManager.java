package me.drendov.XRayMonitor;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.World;

class WorldOreRatesManager
{
    private ConcurrentHashMap<String, WorldOreRates> nameToSettingsMap = new ConcurrentHashMap<String, WorldOreRates>();
    final String worldName = "world";

    WorldOreRatesManager()
    {
        this.nameToSettingsMap.put(this.worldName, new WorldOreRates());
    }
    
    void Set(World world, WorldOreRates settings)
    {
        this.Set(world.getName(), settings);
    }
    
    public void Set(String key, WorldOreRates settings)
    {
        this.nameToSettingsMap.put(key, settings);
    }

    WorldOreRates Get(World world)
    {
        return this.Get(world.getName());
    }

    WorldOreRates Get(String key)
    {
        WorldOreRates settings = this.nameToSettingsMap.get(key);
        if(settings != null)return settings;
        return this.nameToSettingsMap.get(this.worldName);
    }

    public WorldOreRates Create(String worldName)
    {
        WorldOreRates settings = new WorldOreRates();
        this.nameToSettingsMap.put(worldName, settings);
        return settings;
    }
}
