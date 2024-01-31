package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetTeamInfo implements CommandExecutor {
    Game game;
    DunWar plugin;
    public GetTeamInfo(DunWar plugin,Game game){
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            WarPlayer warPlayer = game.FindPlayer(player);
            if(warPlayer.team != null){
                warPlayer.team.GetTeamInfo(sender);
            }
            else{
                sender.sendMessage("You do not belong to a team.");
            }
        }
        return true;
    }
}
