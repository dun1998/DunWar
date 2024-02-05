package io.github.dun1998.dunwar;


import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

public class TeamBase extends MapObject {
    WarTeam ownerTeam;
    ProtectedRegion region;
    TeamSpawn teamSpawn;
    WarpZone warpZone;
    Game game;
    DunWar plugin;

    String name;

    public TeamBase(DunWar plugin,Game game,WarTeam team){
        this.game = game;
        this.plugin = plugin;
        this.ownerTeam = team;
        this.name = this.ownerTeam.name + " Spawn";
    }

    public void setRegion(BlockVector3 minP, BlockVector3 maxP, CommandSender sender) {
        Region select;
        if(this.teamSpawn!=null){
            this.teamSpawn.Destroy();
            this.teamSpawn = null;
        }
        if(this.region!=null){
            this.game.regions.removeRegion(this.region.getId());
        }
        if(minP==null&&maxP==null){
            if(sender instanceof Player){
                select= game.getSelection(sender);
                if(select==null){
                    return;
                }
                minP = select.getMinimumPoint();
                maxP = select.getMaximumPoint();
            }
            else{
                return;
            }
        }
        ProtectedRegion reg = new ProtectedCuboidRegion(this.name.replace(" ",""),BlockVector3.at(minP.getX(),0,minP.getZ()),BlockVector3.at(maxP.getX(),320,maxP.getZ()));
        ApplicableRegionSet appSet = game.regions.getApplicableRegions(reg);
        Set<ProtectedRegion> set = appSet.getRegions();
        for(MapObject obj:game.map.mapObjects){
            if(obj instanceof ControlLand||obj instanceof TeamBase){
                if(set.contains(obj.region)){
                    System.out.println("Overlaps with non overlapping region.");
                    return;
                }
            }
        }
        this.game.regions.addRegion(reg);
        this.region = reg;
    }

    public void SetTeamSpawn(BlockVector3 minP,BlockVector3 maxP,CommandSender sender){
        if(this.region!=null){
            if(minP==null || maxP ==null){
                if(sender instanceof Player){
                    Region select= game.getSelection(sender);
                    if(select==null){
                        return;
                    }
                    else{
                        minP = select.getMinimumPoint();
                        maxP = select.getMaximumPoint();
                    }
                }

            }
            ProtectedRegion reg=  new ProtectedCuboidRegion("test",BlockVector3.at(minP.getX(),0,minP.getZ()),BlockVector3.at(maxP.getX(),320,maxP.getZ()));
            reg.getIntersectingRegions();
            if(this.teamSpawn!=null){
                this.teamSpawn.Destroy();

            }
        }
    }
}
