package io.github.dun1998.dunwar;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;
import com.sk89q.worldedit.regions.CylinderRegion;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GameMap extends GameObject{
    Location center;
    ProtectedRegion region;
    double radius;
    World world;
    Game game;
    public GameMap(Game game){
        this.game = game;
    }
    public void SetMap(Location center, double radius, CommandSender sender){
        this.center = center;
        this.radius = radius;
        World world = center.getWorld();
        this.world = world;
        double centerX = center.getX();
        double centerZ = center.getZ();
        double centerY =  center.getY();
        List<BlockVector2> points = Lists.newArrayList();
        BlockVector3 trueCenter = BlockVector3.at(centerX,0,centerZ);
        int minY = 0;
        int maxY= 140;
        BlockVector3 min = BlockVector3.at(centerX-radius, minY, centerZ-radius);
        BlockVector3 max = BlockVector3.at(centerX+radius, maxY, centerZ+radius);
        ProtectedRegion region = new ProtectedCuboidRegion("gameMap", min, max);


        this.region = region;
        this.region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        this.region.setFlag(Flags.EXIT, StateFlag.State.DENY);
        List<BlockVector2> p = this.region.getPoints();
        for(BlockVector2 b:p){
            sender.sendMessage(String.format("%d,%d",b.getX(),b.getZ()));
        }
        this.game.regions.addRegion(region);
        region.setPriority(1);
        LocalSession session = new LocalSession();
        /*
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
            BlockType b = Objects.requireNonNull(BlockTypes.BEDROCK);
            try {
                editSession.makeCylinder(trueCenter,b.getDefaultState(),radius,maxY,false);// use the edit session here ...
            } catch (MaxChangedBlocksException e) {
                if(sender instanceof Player){
                    sender.sendMessage("Failed to insert blocks");}
                throw new RuntimeException(e);
            }
        }*/ // it is automatically closed/flushed when the code exits the block


        //build the region, blacklist placing or destroying all blocks in the region
        //

    }

    public void DestroyMap(CommandSender sender){
        if (this.region == null) {
            sender.sendMessage("No map to remove");
            return;
        }
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
            BlockType b = Objects.requireNonNull(BlockTypes.AIR);
            try {
                BlockVector3 trueCenter = BlockVector3.at(this.center.getX(),0,this.center.getZ());
                editSession.makeCylinder(trueCenter,b.getDefaultState(),radius,256,false);// use the edit session here ...
            } catch (MaxChangedBlocksException e) {
                if(sender instanceof Player){
                    sender.sendMessage("Failed to insert blocks");}
                throw new RuntimeException(e);
            }
        }
    }
}
