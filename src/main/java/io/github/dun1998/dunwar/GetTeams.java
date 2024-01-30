package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class getAllTeams implements CommandExecutor {

    Game game;
    DunWar plugin;
    public getAllTeams(DunWar plugin,Game game){
        this.game = game;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if(!game.warTeams.isEmpty()){
                List<String> teamNames = new ArrayList<>();
                for(WarTeam team:game.warTeams){
                    teamNames.add(team.name);
                }
                plugin.getLogger().info("Teams found.");

            }
            plugin.getLogger().info("No teams found.");
        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
