package io.github.dun1998.dunwar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarTeam {
    //Name
    //Color
    //Spawn point
    //Score
    String name;
    String color;
    int score;
    Location spawnPoint;
    int id;
    List<WarPlayer> players = new ArrayList<>();
    public WarTeam(String color,int id){
        this.color = color;
        this.name = color;
        this.id =id;
    }

    public void AddPlayer(WarPlayer player){
        players.add((player));
    }
    public void RemovePlayer(WarPlayer player){
        if(players.contains(player)){
            players.remove(player);
        }
    }

}
