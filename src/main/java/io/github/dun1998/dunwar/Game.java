package io.github.dun1998.dunwar;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Game {
    //All the game logic goes here
    List<String> allowedColors = Arrays.asList("blue","green","orange","purple","red","yellow");

    List<WarTeam> warTeams = new ArrayList<>();
    Map<Player,WarPlayer> playerWarDict = new HashMap<>();
    RegionManager regions;
    List<MapObject> gameObjects = new ArrayList<>();
    List<MapObject> requiredGameObjects = new ArrayList<>();
    RegionContainer container;
    World world;
    int minTeamSize =2;
    DunWar plugin;
    private GameMap map;

    public Game(DunWar plugin){
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
                this.plugin.getLogger().info(String.format("DunWar is missing a required object: %s",obj.name));
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

    public void DestroyMap(CommandSender sender){
        this.map.DestroyMap(sender);
    }

}
