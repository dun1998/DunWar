package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetControlLand implements CommandExecutor {
    DunWar plugin;
    Game game;
    public GetControlLand(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(this.game.map!=null){
                this.game.map.GetControlLands(sender);
                return true;
            }
            else{
                sender.sendMessage("Game map not set.");
                return true;
            }
        }
        return true;
    }
}
