package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopGame implements CommandExecutor {
    DunWar plugin;
    Game game;
    public StopGame(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(!game.isRunning){
                sender.sendMessage("No active game detected.");
                return true;
            }
            else{
                sender.sendMessage("Game is stopping...");
                game.GameStop();
                return true;
            }
        }
        return false;
    }
}
