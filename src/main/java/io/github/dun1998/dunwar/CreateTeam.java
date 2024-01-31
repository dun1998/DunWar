package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CreateTeam implements CommandExecutor {

    DunWar plugin;
    Game game;

    public CreateTeam(DunWar plugin, Game game){
        this.plugin = plugin;
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length>0){

            String color = args[0].toLowerCase();
            //First check that team does not already exist.
            if(!game.warTeams.isEmpty()){
                List<String> teamColors = new ArrayList<>();
                for(WarTeam warTeam : game.warTeams){
                    teamColors.add(warTeam.color);
                }
                if(teamColors.contains(color)){
                   sender.sendMessage(String.format("%s is already in use.",color));
                    plugin.getLogger().info(String.format("%s is already in use.",color));
                    //That color is already used by a team, please enter an unused color.
                    return false;
                }
            }
            if(!game.allowedColors.contains(color)){
                //That is not an allowed color
                //These are the allowed colors:
                sender.sendMessage(String.format("%s is not an allowed color. Try /colors",color));
                plugin.getLogger().info(String.format("%s is not an allowed color. Try /colors",color));
                return false;
            }
            else{
                WarTeam t = new WarTeam(color,args.length+1);
                game.warTeams.add(t);
                plugin.getLogger().info(String.format("Created the %s Team",color));
                sender.sendMessage(String.format("Created the %s Team",color));
                return true;
            }

        }
        // If the player (or console) uses our command correct, we can return true
        else{
            return false;
        }
        }
}
