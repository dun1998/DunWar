package io.github.dun1998.dunwar;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ControlLand extends GameObject{
    ControlPoint cp;
    DunWar plugin;
    Game game;
    ProtectedRegion region;
    WarTeam controlTeam;

    boolean canOverlap = true;

    public ControlLand(){

    }
}
