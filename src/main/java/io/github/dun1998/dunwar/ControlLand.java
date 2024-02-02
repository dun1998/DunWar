package io.github.dun1998.dunwar;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import java.util.*;

public class ControlLand extends MapObject {
    ControlPoint cp;
    DunWar plugin;
    Game game;
    ProtectedRegion region;
    WarTeam controlTeam;
    ControlPoint controlPoint; /*
    is a region which when controlled changes the
    */
    String name;
    HashMap<WarTeam,Integer> playersInArea;
    WarTeam currentControlTeam;
    int maxPlayerCount;
    int modRate;
    float hp = 0;
    float maxHp = 100;
    float minHp = 0;
    float swapHp = 10;
    boolean canOverlap = true;
    boolean contested;
    boolean controlTeamPresent;

    public ControlLand(Game game,DunWar plugin,String name,ProtectedRegion region){
        this.game = game;
        this.plugin = plugin;
        this.name = name;
        this.region = region;
    }

    public void ChangeControl(WarTeam team){
        TextComponent msg;
        BlockVector3 maxP = this.region.getMaximumPoint();
        BlockVector3 minP = this.region.getMinimumPoint();
        if(team == null){
            msg = Component.text(this.controlTeam.name).color(this.controlTeam.textColor)
                    .append(Component.text(String.format("has lost control of %s",this.name)));

        }
        else if(controlTeam == null){
            msg = Component.text(team.name).color(team.textColor)
                            .append(Component.text(String.format(" has taken control of %s.",this.name)));
        }
        else {
            msg = Component.text(this.controlTeam.name).color(this.controlTeam.textColor)
                    .append(Component.text(String.format("has lost control of %s to ", this.name)))
                    .append(Component.text(team.name).color(team.textColor))
                    .append(Component.text("."));
        }
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(game.world))) {
            CuboidRegion cuboidRegion = new CuboidRegion(minP,maxP);
            BlockType blockTo = Objects.requireNonNull(game.woolColor.get(team==null? "white":team.color.toLowerCase()));
            BlockType blockFrom  = Objects.requireNonNull(game.woolColor.get(controlTeam==null? "white":controlTeam.color.toLowerCase()));
            BlockState bs = blockTo.getDefaultState();
            BlockState bt = blockFrom.getDefaultState();
            Set<BaseBlock> replBlock = new HashSet<>();
            replBlock.add(blockFrom.getDefaultState().toBaseBlock());
            editSession.replaceBlocks(cuboidRegion,replBlock,blockTo.getDefaultState());
        } // it is automatically closed/flushed when the code exits the block
        catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
        this.controlTeam = team;
        this.hp = team==null? this.minHp:this.swapHp;
        plugin.getServer().broadcast(msg);
    }

    public void AddControlPoint(){

    }
    public void Update(){
        TextComponent msg;
        msg = Component.text(String.format("Checking status of %s",this.name));
        plugin.getServer().broadcast(msg);
        this.CheckPlayers();
        if(this.playersInArea.isEmpty()){
            msg = Component.text(String.format("No players in control point of %s",this.name));
            plugin.getServer().broadcast(msg);
            return;
        }
        msg = Component.text(String.format("Players detected in control point of %s",this.name));
        plugin.getServer().broadcast(msg);
        this.CheckControlChange();
        this.ChangeHealth();
    }
    public void CheckPlayers(){
        this.playersInArea = new HashMap<WarTeam,Integer>();
        if(this.controlPoint!= null){
            if(this.controlPoint.region!=null){
                for(WarPlayer p:this.game.playerWarDict.values()){
                    System.out.println(p.player.getName());
                    if(p.team!=null){
                        Location l  = p.player.getLocation();
                        BukkitAdapter.adapt(l);
                        if(this.controlPoint.region.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ())){
                            if(playersInArea.containsKey(p.team)){
                                playersInArea.put(p.team,playersInArea.get(p.team)+1);
                            }
                            else{
                                playersInArea.put(p.team,1);
                            }
                        }
                    }
                }
            }
            else{
                System.out.println("Cp has no region.");
            }
        }
        else{
            System.out.println("No cp detected.");
        }
    }

    public void CheckControlChange(){
        this.controlTeamPresent  = playersInArea.containsKey(controlTeam);
        this.contested = playersInArea.keySet().size()>1;
        this.maxPlayerCount=0;
        for(WarTeam t:playersInArea.keySet()){
            if(playersInArea.get(t)>maxPlayerCount) {
                maxPlayerCount = playersInArea.get(t);
                this.currentControlTeam = t;
            }
        }

    }

    public void ChangeHealth(){
        TextComponent msg;
        float prevHp = hp;
        if(this.controlTeamPresent&&!this.contested){
            this.hp = Math.min(this.hp+(game.captureRate * this.maxPlayerCount),maxHp);
        }
        else if(this.controlTeam==null&&!this.contested){
                this.ChangeControl(this.currentControlTeam);
        }
        else if(!this.controlTeamPresent){
            this.hp = Math.max(this.minHp,(this.hp-game.captureRate*this.maxPlayerCount));
        }
        if(this.hp == 0){
            System.out.println("Changing control");
            this.ChangeControl(null);
        }
        if(prevHp>this.hp){
            msg = Component.text(this.controlTeam.name).color(this.controlTeam.textColor)
                    .append(Component.text(String.format(" has lost %f hp on %s, new hp is %f ", prevHp - this.hp,this.name,this.hp)));
            plugin.getServer().broadcast(msg);
        }
        else if(prevHp<this.hp){
            msg = Component.text(this.controlTeam.name).color(this.controlTeam.textColor)
                    .append(Component.text(String.format(" has gained %f hp on %s, new hp is %f ", this.hp-prevHp,this.name,this.hp)));
            plugin.getServer().broadcast(msg);

        }
    }
}
