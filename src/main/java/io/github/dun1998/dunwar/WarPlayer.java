package io.github.dun1998.dunwar;

import org.bukkit.entity.Player;

import java.util.UUID;

public class WarPlayer {
    // A player should have the following:
    // A team, a spawn point
    // A load function?
    //Stats
    Player player;
    WarTeam team;
    private int score=0;
    public WarPlayer(Player player){
        this.player = player;
    }
    public void AssignTeam(WarTeam team){
        this.team = team;
    }

    public void addScore(int increment){
        this.score += increment;
    }

    public void setScore(int newScore){
        this.score = newScore;
    }
}
