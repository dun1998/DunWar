package io.github.dun1998.dunwar;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

public class ControlLand extends MapObject {
    List<WarPlayer> playersInLand= new ArrayList<>();
    ControlPoint cp;
    DunWar plugin;
    Game game;
    ProtectedRegion region;
    WarTeam controlTeam;
    ControlPoint controlPoint; /*
    is a region which when controlled changes the
    */
    String name;
    HashMap<WarTeam,Integer> playersPerTeamInArea = new HashMap<>();
    List<WarPlayer> playersInArea = new ArrayList<>();
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
    Component barName = Component.text("Neutral control");
    BossBar controlBar = BossBar.bossBar(barName, 0,BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);

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
        List<WarPlayer>prevPlayerList;
        msg = Component.text(String.format("Checking status of %s",this.name));
        this.GetEntryPlayers();
        if(!this.playersInArea.isEmpty()){
            prevPlayerList = new ArrayList<>(this.playersInArea);
        }
        else{
            prevPlayerList = new ArrayList<>();
        }
        plugin.getServer().broadcast(msg);
        this.CheckPlayers();
        this.UpdateControlBarVisibility(prevPlayerList);
        if(this.playersPerTeamInArea.isEmpty()){
            msg = Component.text(String.format("No players in control point of %s",this.name));
            plugin.getServer().broadcast(msg);
            return;
        }
        msg = Component.text(String.format("Players detected in control point of %s",this.name));
        plugin.getServer().broadcast(msg);
        this.CheckControlChange();
        this.ChangeHealth();
        this.UpdateControlBar();
    }
    public void CheckPlayers(){
        this.playersPerTeamInArea = new HashMap<WarTeam,Integer>();
        this.playersInArea = new ArrayList<>();
        if(this.controlPoint!= null){
            if(this.controlPoint.region!=null){
                for(WarPlayer p:this.game.playerWarDict.values()){
                    System.out.println(p.player.getName());
                    if(p.team!=null){
                        Location l  = p.player.getLocation();
                        BukkitAdapter.adapt(l);
                        if(this.controlPoint.region.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ())){
                            this.playersInArea.add(p);
                            if(playersPerTeamInArea.containsKey(p.team)){
                                playersPerTeamInArea.put(p.team, playersPerTeamInArea.get(p.team)+1);
                            }
                            else{
                                playersPerTeamInArea.put(p.team,1);
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
        this.controlTeamPresent  = playersPerTeamInArea.containsKey(controlTeam);
        this.contested = playersPerTeamInArea.keySet().size()>1;
        this.maxPlayerCount=0;
        for(WarTeam t: playersPerTeamInArea.keySet()){
            if(playersPerTeamInArea.get(t)>maxPlayerCount) {
                maxPlayerCount = playersPerTeamInArea.get(t);
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
    public void UpdateControlBarVisibility(List<WarPlayer> prevPlayers){
        if(!this.playersInArea.isEmpty()){
            for(WarPlayer curPlayer:this.playersInArea){
                if(!prevPlayers.contains(curPlayer)){
                    //show bar
                    curPlayer.player.showBossBar(this.controlBar);
                }
                else{
                    prevPlayers.remove(curPlayer);
                }
            }
        }
        if(!prevPlayers.isEmpty()){
            for(WarPlayer prevPlayer:prevPlayers){
                prevPlayer.player.hideBossBar(this.controlBar);
            }
        }
    }
    public void UpdateControlBar(){
        this.controlBar.progress(this.hp/this.maxHp);
        if(this.controlTeam!=null){
            this.controlBar.color(this.controlTeam.barColor);
            this.controlBar.name(Component.text(this.controlTeam.name.toUpperCase() + " CONTROL"));
        }
        else{
            this.controlBar.color(BossBar.Color.WHITE);
            this.controlBar.name(Component.text("NEUTRAL CONTROL"));
        }

    }

    public void ShowTitle(Player target){
        Component entryTitle;
        Component subTitle;
        if(this.controlTeam!=null) {
            entryTitle = Component.text(this.name.toUpperCase(), this.controlTeam.textColor);
            subTitle = Component.text(String.format("Controlled by: %s",this.controlTeam.name), this.controlTeam.textColor);
        }
        else{
            entryTitle = Component.text(this.name.toUpperCase(), NamedTextColor.WHITE);
            subTitle = Component.text("Neutral lands", NamedTextColor.WHITE);
        }
        Title t;
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(0), Duration.ofMillis(1000));
        t = Title.title(entryTitle,subTitle,times);
        target.showTitle(t);
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
}
