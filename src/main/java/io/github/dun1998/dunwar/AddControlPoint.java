package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AddControlPoint implements CommandExecutor {
    DunWar plugin;
    Game game;
    public AddControlPoint(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ControlLand cl = null;
        if(game.map == null){
            sender.sendMessage("Set the game map first.");
            return true;
        }
        if(args.length>=1){
            String clName = args[0];
            if(game.map.mapObjects.isEmpty()){
                sender.sendMessage("Create a control land first.");
                return true;
            }
            for(MapObject obj:game.map.mapObjects){
                if(obj instanceof ControlLand){
                    if((((ControlLand) obj).name.equalsIgnoreCase(clName))){
                        cl = (ControlLand) obj;
                    }
                }
            }
            if(cl==null){
                sender.sendMessage(String.format("Failed to find control land %s.",clName));
                return true;
            }
            else{
                ControlPoint cp = new ControlPoint(game);
                boolean conf = cp.AddRegion(sender,cl);
                if(conf){
                    game.map.mapObjects.add(cp);
                }
                else{
                    return true;
                }
                sender.sendMessage(String.format("%s added as a controlpoint.",cp.region.getId()));
                return true;
            }
        }
        return false;
    }
}
