package io.github.dun1998.dunwar;

import org.bukkit.Location;

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

    public WarTeam(String color){
        this.color = color;
    }
}
