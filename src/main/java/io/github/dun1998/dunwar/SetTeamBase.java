package io.github.dun1998.dunwar;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetTeamBase implements CommandExecutor {

    DunWar plugin;
    Game game;
    public  SetTeamBase(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game= game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(args.length<1){
                return false;
            }
            if(this.game.map==null){
                sender.sendMessage("Please set the game map before adding a team's base.");
                return true;
            }
            String teamName = args[0];
            List<String> teamNames = new ArrayList<>();
            for(WarTeam team: game.warTeams){
                if(team.name.equalsIgnoreCase(teamName)) {
                    if(team.base!=null){
                        team.base.Destroy();
                    }
                    team.base = new TeamBase(plugin,game, team,sender);
                    team.base.setRegion(null,null,sender);
                    if(team.base.region==null){
                        team.base.Destroy();
                        team.base =null;
                        sender.sendMessage("Failed to initialize the region for the team's base.");
                    }
                    else{
                        sender.sendMessage(String.format("Set a new base for %s",team.name));
                    }
                    return true;
                }
            }
            sender.sendMessage("That team name doesn't exist in the current game.");
            return true;
        }
        return false;
    }
}
