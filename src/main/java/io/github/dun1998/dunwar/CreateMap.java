package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateMap implements CommandExecutor {
    DunWar plugin;
    Game game;
    public CreateMap(DunWar plugin, Game game){
        this.plugin = plugin;
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            if(args.length>=1){
                Player player = ((Player) sender).getPlayer();
                String rad = args[0];
                double radius;
                try{
                radius = Double.parseDouble(rad);}
                catch (Exception e){
                    return false;
                }
                GameMap map = this.game.GetMap();
                if(map.region != null){
                    sender.sendMessage("Game map already set.");
                    return true;
                }
                map.SetMap(player.getLocation(),radius,sender);
                sender.sendMessage("Game map created");
                return true;
            }
            else{
                return false;
            }
        }
        return true;
    }
}
