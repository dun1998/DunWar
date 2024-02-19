package io.github.dun1998.dunwar;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TeamBase extends MapObject {
    WarTeam ownerTeam;
    ProtectedRegion region;
    Location teamSpawn;
    WarpZone warpZone;
    DunWar plugin;
    String name;
    List<WarPlayer> playersInLand =new ArrayList<>();

    public TeamBase(DunWar plugin,Game game,WarTeam team,CommandSender sender){
        this.ownerTeam = team;
        this.name = this.ownerTeam.name + " Area";
        this.game = game;
        this.plugin = plugin;
        this.game.map.mapObjects.add(this);
    }
    public void setRegion(BlockVector3 minP, BlockVector3 maxP, CommandSender sender) {
        Region select;
        this.teamSpawn = null;
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
        reg.setFlag(Flags.ENTRY, StateFlag.State.DENY);
        reg.setFlag(Flags.ENTRY_DENY_MESSAGE,"A magical force prevents you from entering this team's base.");
        this.game.regions.addRegion(reg);
        this.region = reg;
        this.ownerTeam.UpdatePlayers();
    }

    public void SetTeamSpawn(CommandSender sender){
        if(this.region!=null){
            if(sender instanceof Player){
                Location loc = ((Player) sender).getLocation();
                BlockVector3 blockVector3 = BlockVector3.at(loc.getX(),loc.getY(),loc.getZ());
                if(!this.region.contains(blockVector3)){
                    sender.sendMessage("Spawn point not within team base region.");
                }
                else{
                    this.teamSpawn= loc;
                    ownerTeam.UpdatePlayers();
                    sender.sendMessage(String.format("Team spawn set at: %f %f %f",loc.getX(),loc.getY(),loc.getZ()));
                }
            }
        }
        else{
            sender.sendMessage("Please set a region for the team's spawn.");
        }
    }

    public void Destroy(){
        this.teamSpawn = null;
        if(this.region!=null){
            game.regions.removeRegion(this.region.getId());
        }
        this.game.map.mapObjects.remove(this);
    }

    @Override
    public void Update(){
        this.GetEntryPlayers();
    }

    public void GetEntryPlayers(){
        List<WarPlayer> prevPlayers = new ArrayList<>(this.playersInLand);
        this.playersInLand = new ArrayList<>();
        for(WarPlayer p:this.game.playerWarDict.values()) {
            System.out.println(p.player.getName());
            if (p.team != null) {
                Location l = p.player.getLocation();
                BukkitAdapter.adapt(l);
                if (this.region.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
                    this.playersInLand.add(p);
                }
            }
        }
        if(!this.playersInLand.isEmpty()){
            for(WarPlayer curPlayer:this.playersInLand){
                if(!prevPlayers.contains(curPlayer)){
                    this.ShowTitle(curPlayer.player);
                }
            }
        }
    }

    public void ShowTitle(Player target){
        Component entryTitle;
        Component subTitle;
        entryTitle = Component.text(String.format("%s's base.",this.ownerTeam.name), this.ownerTeam.textColor);
        subTitle = Component.text("Home sweet home.", this.ownerTeam.textColor);
        Title t;
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1500), Duration.ofMillis(1000));
        t = Title.title(entryTitle,subTitle,times);
        target.showTitle(t);
    }

}
