package io.github.dun1998.dunwar;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game {
    //All the game logic goes here
    List<String> allowedColors = Arrays.asList("blue","green","purple","red","yellow");
    Map<String, BlockType> woolColor = new HashMap<>();
    List<WarTeam> warTeams = new ArrayList<>();
    Map<Player,WarPlayer> playerWarDict = new HashMap<>();
    RegionManager regions;
    List<MapObject> gameObjects = new ArrayList<>();
    List<MapObject> requiredGameObjects = new ArrayList<>();
    RegionContainer container;
    List<BukkitTask> activeTasks = new ArrayList<>();
    World world;
    float captureRate = 10;
    int minTeamSize =2;
    DunWar plugin;
    public GameMap map;
    boolean isRunning= false;

    public Game(DunWar plugin){
        woolColor.put("white",BlockTypes.WHITE_WOOL);
        woolColor.put("blue",BlockTypes.BLUE_WOOL);
        woolColor.put("green",BlockTypes.GREEN_WOOL);
        woolColor.put("orange",BlockTypes.ORANGE_WOOL);
        woolColor.put("purple",BlockTypes.PURPLE_WOOL);
        woolColor.put("red",BlockTypes.RED_WOOL);
        woolColor.put("yellow",BlockTypes.YELLOW_WOOL);
        this.plugin = plugin;
        this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.world = Bukkit.getServer().getWorlds().get(0);
        this.regions = container.get(BukkitAdapter.adapt(this.world));
    }
    public WarPlayer FindPlayer(Player player){
        if(this.playerWarDict.containsKey(player)){
            return playerWarDict.get(player);
        }
        else{
            WarPlayer warPlayer = new WarPlayer(player);
            playerWarDict.put(player,warPlayer);
            return warPlayer;
        }
    }
    public void GameStart(){
        this.isRunning = true;
        BukkitTask t;
        if(!map.mapObjects.isEmpty()){
            for(MapObject obj :map.mapObjects){
                if(obj instanceof ControlLand){
                    t = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> ((ControlLand) obj).Update(),0L,20L);
                    this.activeTasks.add(t);
                }
            }
        }
    }

    public void GameStop(){
        this.isRunning = false;
        for(BukkitTask task:this.activeTasks){
            task.cancel();
        }
        this.activeTasks.clear();
    }
    public boolean ReadyCheck(){
        boolean ready = true;
        List<MapObject> missingObj = new ArrayList<>();
        for(MapObject obj:this.requiredGameObjects){
            if(!this.gameObjects.contains((obj))){
                missingObj.add(obj);
            }
        }
        if(!missingObj.isEmpty()){
            ready = false;
            for(MapObject obj:missingObj){
                this.plugin.getLogger().info(String.format("DunWar is missing a required object: %s","MapOBj"));
            }
        }
        if(this.warTeams.size()<minTeamSize){
            ready = false;
        }
        else{
            for(WarTeam team:this.warTeams){
                //check for base
                //check base for spawn
            }
        }
        return ready;
    }

    public GameMap GetMap(){
        if(this.map == null){
            this.map = new GameMap(this);
        }
        return this.map;
    }
    public Region getSelection(CommandSender sender){
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(sender));
        Region region; // declare the region variable
        // note: not necessarily the player's current world, see the concepts page
        try {
            region = localSession.getSelection(BukkitAdapter.adapt(this.world));
            sender.sendMessage(region.getMinimumPoint().toString());
            sender.sendMessage(region.getMaximumPoint().toString());
            return region;
        } catch (IncompleteRegionException ex) {
            sender.sendMessage("Please make a region selection first.");
            return null;
        }
    }
    public void DestroyMap(CommandSender sender){
        this.map.DestroyMap(sender);
    }

}
