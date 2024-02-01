package io.github.dun1998.dunwar;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class ControlLand extends MapObject {
    ControlPoint cp;
    DunWar plugin;
    Game game;
    ProtectedRegion region;
    WarTeam controlTeam;
    ControlPoint controlPoint; /*
    is a region which when controlled changes the
    */
    String name;
    boolean canOverlap = true;

    public ControlLand(String name){
        this.name = name;
    }

    public void ChangeControl(WarTeam team){
        TextComponent msg;
        if(controlTeam == null){
            msg = Component.text(team.name).color(team.textColor)
                            .append(Component.text(String.format(" has taken control of %s.",this.name)));
        }
        else {
            msg = Component.text(this.controlTeam.name).color(this.controlTeam.textColor)
                    .append(Component.text(String.format("has lost control of %s to ", this.name)))
                    .append(Component.text(team.name).color(team.textColor))
                    .append(Component.text("."));
        }
        this.controlTeam = team;
        plugin.getServer().broadcast(msg);
    }

    public void AddControlPoint(){

    }
}
