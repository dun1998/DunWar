package io.github.dun1998.dunwar;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

public class TeamSpawn extends MapObject{
    String name;
    ProtectedRegion region;
    Game game;
    WarTeam ownerTeam;
    TeamBase base;
    Location spawnPoint;

    public TeamSpawn(ProtectedRegion reg,Game game,WarTeam ownerTeam,TeamBase base,Location spawnPoint){
        this.region = reg;
        this.game = game;
        this.ownerTeam = ownerTeam;
        this.base = base;
        this.spawnPoint = spawnPoint;
    }
    public void Destroy(){
        this.base = null;
        this.name = null;
        game.regions.removeRegion(region.getId());
    }
}
