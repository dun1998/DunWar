package io.github.dun1998.dunwar;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.CommandSender;

public abstract class MapObject {
    //an object which is part of the game (spawn,control point,storage
    boolean required= false;
    boolean enabled= false;
    String name;
    Game game;
    boolean canOverlap = true;
    ProtectedRegion region;

    public Region getSelection(CommandSender sender){
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(sender));
        Region region; // declare the region variable
        // note: not necessarily the player's current world, see the concepts page
        try {
            region = localSession.getSelection(BukkitAdapter.adapt(this.game.world));
            return region;
        } catch (IncompleteRegionException ex) {
            sender.sendMessage("Please make a region selection first.");
            return null;
        }
    }


}
