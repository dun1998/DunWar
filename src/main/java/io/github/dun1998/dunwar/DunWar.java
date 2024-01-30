package io.github.dun1998.dunwar;


import org.bukkit.plugin.java.JavaPlugin;

public final class DunWar extends JavaPlugin {

    @Override
    public void onEnable() {
        Game game = new Game(this);
        this.getCommand("Teams").setExecutor(new GetTeams(this,game));
        this.getCommand("CreateTeam").setExecutor(new CreateTeam(this,game));
        this.getCommand("RenameTeam").setExecutor(new TeamRename(this,game));
        this.getCommand("JoinTeam").setExecutor(new JoinTeam(this,game));


        // Plugin startup logic
        //Load/Setup database
        //Check if a game is running
        //If so load the game and its stats
        //
        //Implement commands to build the game structure
        //
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
