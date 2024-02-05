package io.github.dun1998.dunwar;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class DunWar extends JavaPlugin {
    Game game;
    @Override
    public void onEnable() {
        Game game = new Game(this);
        this.game = game;
        Map<String, ProtectedRegion> regs = game.regions.getRegions();
        for(ProtectedRegion region:regs.values()){
            game.regions.removeRegion(region.getId());
        }
        this.getCommand("Teams").setExecutor(new GetTeams(this,game));
        this.getCommand("CreateTeam").setExecutor(new CreateTeam(this,game));
        this.getCommand("RenameTeam").setExecutor(new TeamRename(this,game));
        this.getCommand("JoinTeam").setExecutor(new JoinTeam(this,game));
        this.getCommand("Team").setExecutor(new GetTeamInfo(this,game));
        this.getCommand("SetMap").setExecutor(new CreateMap(this,game));
        this.getCommand("CreateControlLand").setExecutor(new CreateControlLand(this,game));
        this.getCommand("GetControlLands").setExecutor(new GetControlLand(this,game));
        this.getCommand("AddControlPoint").setExecutor(new AddControlPoint(this,game));
        this.getCommand("StartGame").setExecutor(new StartGame(this,game));
        this.getCommand("StopGame").setExecutor(new StopGame(this,game));
        // Plugin startup logic
        //Load/Setup database
        //Check if a game is running
        //If so load the game and its stats
        //
        //Implement commands to build the game structure
        //
    }

    @Override
    public void onDisable() {
        if(game.isRunning){
            game.GameStop();
        }
        // Plugin shutdown logic
        /*
        if(game.map !=null) {
            if (game.map.mapObjects.size() > 0) {
                for (MapObject obj : game.map.mapObjects) {
                    if(this.game.regions.hasRegion(obj.name)){
                        this.game.regions.removeRegion(obj.name);
                    }
                }
            }
        }*/
    }
}
