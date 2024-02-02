package io.github.dun1998.dunwar;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateControlLand implements CommandExecutor {
    Game game;
    DunWar plugin;
    public CreateControlLand(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //get points
        if(sender instanceof Player){
            if(args.length>=1){
                String name= args[0];
                Region region = game.getSelection(sender);
                if(region == null){
                    sender.sendMessage("Please make a selection with the world edit wand.");
                    return true;
                }
                BlockVector3 maxP = region.getMinimumPoint();
                BlockVector3 minP = region.getMaximumPoint();
                BlockVector3 min = BlockVector3.at(minP.getX(), 0, minP.getZ());
                BlockVector3 max = BlockVector3.at(maxP.getX(), 256, maxP.getZ());
                sender.sendMessage(min.toString());
                sender.sendMessage(max.toString());
                ProtectedRegion check = new ProtectedCuboidRegion(name, min, max);
                ApplicableRegionSet applicationSet = game.regions.getApplicableRegions(check);
                List<ProtectedRegion> regs = Lists.newArrayList(applicationSet);
                if(game.map!=null){
                    if(!regs.contains(game.map.region)){
                        sender.sendMessage("Does not overlap with the game map.");
                        return true;
                    }
                    for(MapObject obj:game.map.mapObjects){
                        if(!obj.canOverlap){
                            if(regs.contains(obj.region)){
                                sender.sendMessage("Overlapping with a non-overlap region.");
                                return true;
                            }
                        }
                        if(obj.region.getId().equalsIgnoreCase(check.getId())){
                            sender.sendMessage("Name is already in use.");
                            return true;
                        }
                    }
                }
                else{sender.sendMessage("Please create a game map first"); return true;}
                ControlLand c = new ControlLand(this.game,this.plugin,name,check);
                this.game.regions.addRegion(check);
                game.map.mapObjects.add(c);
                sender.sendMessage(String.format("Created a control land: %s",name));
                return true;
            }
            else{
                sender.sendMessage("Please supply a ControlLand name");
                return false;
            }

        }
        //check region
        return false;
    }
}
