package io.github.dun1998.dunwar;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
    //All the game logic goes here
    List<String> allowedColors = Arrays.asList("blue","green","yellow");
    List<WarTeam> warTeams = new ArrayList<>();
    Map<Player,WarPlayer> playerWarDict = new HashMap<>();

    List<GameObject> gameObjects = new ArrayList<>();
    List<GameObject> requiredGameObjects = new ArrayList<>();
    int minTeamSize =2;
    DunWar plugin;

    public Game(DunWar plugin){
        this.plugin = plugin;
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
        List<GameObject> missingObj = new ArrayList<>();
        for(GameObject obj:this.requiredGameObjects){
            if(!this.gameObjects.contains((obj))){
                missingObj.add(obj);
            }
        }
        if(!missingObj.isEmpty()){
            ready = false;
            for(GameObject obj:missingObj){
                this.plugin.getLogger().info(String.format("DunWar is missing a required object: %s",obj.name));
            }
        }
        if(this.warTeams.size()<minTeamSize){
            ready = false;
        }
        return ready;
    }

}
