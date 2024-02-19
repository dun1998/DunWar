package io.github.dun1998.dunwar;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControlPoint extends MapObject{
    ProtectedRegion region;
    Game game;
    ControlLand parentLand;
    public ControlPoint(Game game){
        this.game = game;
    }
    public Boolean AddRegion(CommandSender sender, ControlLand controlLand){
        Region r = game.getSelection(sender);
        List<String> regNames = new ArrayList<>();
        String name = "ControlP" + controlLand.region.getId();
        if(game.regions.size()==0){
            sender.sendMessage("No control lands exist.");
            return false;
        }
        else{
            Map<String,ProtectedRegion> prs = game.regions.getRegions();
            for(ProtectedRegion reg:prs.values()){
                regNames.add(reg.getId().toLowerCase());
            }
            if(regNames.contains(name.toLowerCase())){
                sender.sendMessage(String.format("%s already in use for a map object.",name));
                return false;
            }
        }
        if(r == null){
            return false;
        }
        BlockVector3 minP = BlockVector3.at(r.getMinimumPoint().getX(),0,r.getMinimumPoint().getZ());
        BlockVector3 maxP = BlockVector3.at(r.getMaximumPoint().getX(),256,r.getMaximumPoint().getZ());
        ProtectedRegion test = new ProtectedCuboidRegion(String.format(name), minP, maxP);
        ApplicableRegionSet set = game.regions.getApplicableRegions(test);
        if (set.size() == 0) {
            sender.sendMessage("There are no control lands.");
            return false;
        }
        Set<ProtectedRegion> setProt = set.getRegions();
        if(setProt.contains(controlLand.region)){
            if(controlLand.controlPoint!=null){
                controlLand.controlPoint.RemoveRegion(sender);
            }
            this.region = test;
            game.regions.addRegion(this.region);
            this.parentLand = controlLand;
            this.parentLand.controlPoint = this;
            return true;
        }
        else{
            sender.sendMessage("Selected control point does not overlap with selected control land.");
        return false;}
    }

    public void RemoveRegion(CommandSender sender){
        if(this.region != null) {
            sender.sendMessage(String.format("%s removed.",this.region.getId()));
            game.regions.removeRegion(this.region.getId());
            this.region = null;
            this.parentLand.controlPoint = null;
            this.parentLand = null;
        }
    }

}
