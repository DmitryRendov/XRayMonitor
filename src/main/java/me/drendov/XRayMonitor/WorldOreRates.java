package me.drendov.XRayMonitor;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

class WorldOreRates
{
    ArrayList<Material> stoneBlocks;
    ConcurrentHashMap<Material, ArrayList<OreRate>> oreRates;
}

class OreRate {
    public Double warn;
    public Double confirmed;
    public Integer weight;

    OreRate(Double warn, Double confirmed, Integer weight){
        this.warn = warn;
        this.confirmed = confirmed;
        this.weight = weight;
    }
}