package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class JoinTeam implements CommandExecutor {
    DunWar plugin;
    Game game;
    public JoinTeam(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String Label,String[] args){
        WarTeam assignedTeam = null;
        if(sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            WarPlayer warPlayer = game.FindPlayer(player);
            if(game.warTeams.size()>=1) {
                if (args.length == 0) {
                    int minPlayers = 10000;
                    assignedTeam = game.warTeams.get(0);
                    for (WarTeam team : game.warTeams) {
                        if (team.players.size() < minPlayers) {
                            minPlayers = team.players.size();
                            assignedTeam = team;
                        }
                    }
                }
                else{
                    String match = args[0].toLowerCase();
                    Map<String,WarTeam> dictionary = new HashMap<>();
                    for(WarTeam team:game.warTeams){
                        dictionary.put(team.name.toLowerCase(),team);
                    }
                    if(dictionary.containsKey(match)){
                        assignedTeam = dictionary.get(match);
                    }
                    else{
                        plugin.getLogger().info("Team name entered could not be found.");
                        sender.sendMessage("Team name entered could not be found.");
                        return false;
                    }
                }
                if(warPlayer.team != null){
                    warPlayer.team.RemovePlayer(warPlayer);
                }
                assignedTeam.AddPlayer(warPlayer);
                warPlayer.team = assignedTeam;
                plugin.getLogger().info(String.format("Assigned to team %s",assignedTeam.name));
                sender.sendMessage(String.format("Assigned to team %s",assignedTeam.name));
            }
            else{
                plugin.getLogger().info("No teams exist yet. Create one.");
                sender.sendMessage("No teams exist yet. Create one.");
                return false;
            }
        }
        else{
            plugin.getLogger().info("Only players may use this command.");
            sender.sendMessage("Only players may use this command.");
        }
        return true;
    }
}
