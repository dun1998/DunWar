package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetTeamSpawn implements CommandExecutor {

    DunWar plugin;
    Game game;
    public SetTeamSpawn(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(args.length>0){
                String teamName = args[0];
                for(WarTeam team:game.warTeams){
                    if(team.name.equalsIgnoreCase(teamName)){
                        if(team.base==null){
                            sender.sendMessage("Please set a team base first.");
                            return true;
                        }
                        team.base.SetTeamSpawn(sender);
                        return true;
                    }
                }
                sender.sendMessage(String.format("Failed to locate a team with the name %s",teamName));
                return true;
            }
            else{
                return false;
            }
        }
        return true;
    }
}
