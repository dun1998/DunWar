package io.github.dun1998.dunwar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartGame implements CommandExecutor {
    DunWar plugin;
    Game game;
    public StartGame(DunWar plugin,Game game){
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(game.isRunning){
                sender.sendMessage("Game is already running.");
                return true;
            }
            else{
                sender.sendMessage("Game is starting...");
                game.GameStart();
                return true;
            }
        }
        return false;
    }
}
