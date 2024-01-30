package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TeamRename implements CommandExecutor {

    Game game;
    DunWar plugin;
    public TeamRename(DunWar plugin, Game game){
        this.plugin = plugin;
        this.game = game;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        List<String> oldTeamNames = new ArrayList<>();
        String teamName;
        String teamColor;
        if(args.length>1){
            String match  =args[0].toLowerCase();
            String newName = args[1];
            WarTeam targetTeam = null;
            for(WarTeam team:game.warTeams){
                teamName = team.name.toLowerCase();
                teamColor = team.color.toLowerCase();
                if(match.equals(teamName) || match.equals(teamColor)){
                    targetTeam = team;
                }
                oldTeamNames.add(teamName);
                oldTeamNames.add(teamColor);
            }
            if(targetTeam != null){
                if(!oldTeamNames.contains(newName.toLowerCase())){
                    plugin.getLogger().info("Team renamed from "+ targetTeam.name+" to " + newName);
                    targetTeam.name = newName;
                    return true;
                }
                else{
                    plugin.getLogger().info("Team name already used.");
                    return false;
                }
            }
            else{
                plugin.getLogger().info(String.format("No matching team for input: %s",match));
                return false;
            }
        }
        else{
            plugin.getLogger().info("Missing parameters.");
            return false;
        }
    }
}
