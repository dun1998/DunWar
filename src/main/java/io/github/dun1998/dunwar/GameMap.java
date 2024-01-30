package io.github.dun1998.dunwar;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GameMap extends GameObject{

    public void SetMap(Location center,int radius){
        World world = center.getWorld();
        double centerX = center.getX();
        double centerZ = center.getZ();
        List<BlockVector2> points = Lists.newArrayList();
        points.add(BlockVector2.at(centerX + radius, centerZ+ radius));
        points.add(BlockVector2.at(centerX + radius, centerZ- radius));
        points.add(BlockVector2.at(centerX - radius, centerZ+ radius));
        points.add(BlockVector2.at(centerX - radius, centerZ- radius));
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        //build the region, blacklist placing or destroying all blocks in the region
        //

    }
}
