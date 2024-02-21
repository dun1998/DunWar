package io.github.dun1998.dunwar;


import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarTeam {
    //Name
    //Color
    //Spawn point
    //Score
    String name;
    String color;
    int score;
    int id;
    NamedTextColor textColor;
    List<WarPlayer> players = new ArrayList<>();
    BossBar.Color barColor;
    Map<String, NamedTextColor> textColorConversion = new HashMap<String, NamedTextColor>();
    Map<String, BossBar.Color> barColorConversion = new HashMap<String, BossBar.Color>();

    TeamBase base;
    private com.sk89q.worldguard.bukkit.WorldGuardPlugin WorldGuardPlugin;

    public WarTeam(String color,int id){
        textColorConversion.put("red", NamedTextColor.RED);
        textColorConversion.put("blue", NamedTextColor.BLUE);
        textColorConversion.put("purple", NamedTextColor.LIGHT_PURPLE);
        textColorConversion.put("green", NamedTextColor.GREEN);
        textColorConversion.put("yellow", NamedTextColor.YELLOW);
        barColorConversion.put("red", BossBar.Color.RED);
        barColorConversion.put("blue", BossBar.Color.BLUE);
        barColorConversion.put("purple", BossBar.Color.PURPLE);
        barColorConversion.put("green", BossBar.Color.GREEN);
        barColorConversion.put("yellow", BossBar.Color.YELLOW);
        barColorConversion.put("white", BossBar.Color.WHITE);
        this.color = color.toLowerCase();
        this.name = color;
        this.id =id;
        if(textColorConversion.containsKey(this.color)){
            this.textColor = textColorConversion.get(this.color);
        }
        if(barColorConversion.containsKey(this.color)){
            this.barColor = barColorConversion.get(this.color);
        }
    }

    public void AddPlayer(WarPlayer player){
        players.add((player));
        if(this.base!=null){
            if(base.teamSpawn!=null){
            player.player.setRespawnLocation(base.teamSpawn,true);
            }
            if(this.base.region!=null){
                DefaultDomain members = this.base.region.getMembers();
                LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player.player);
                members.addPlayer(lp);
            }
        }
    }

    public void UpdatePlayers(){
        for(WarPlayer player: this.players){
            Player p = player.player;
            if(this.base!=null){
                if(this.base.region!=null){
                    LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(p);
                    if(!this.base.region.isMember(lp)){
                        DefaultDomain members = this.base.region.getMembers();
                        members.addPlayer(lp);
                    }
                }
                if(this.base.teamSpawn!=null){
                    p.setRespawnLocation(base.teamSpawn,true);
                }
            }
        }
    }
    public void RemovePlayer(WarPlayer player){
        players.remove(player);
        if(this.base!=null&&this.base.region!=null){
            DefaultDomain members = this.base.region.getMembers();
            LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player.player);
            members.removePlayer(lp);
        }

    }

    public void GetTeamInfo(CommandSender sender){
        //sends the player all the team info
        if(sender instanceof Player){
            sender.sendMessage(String.format("~~~~~~~~~~~~~~~~~~\nTeam: %s \nColor: %s",this.name,this.color));
        }
    }

    public List<WarPlayer> getPlayers() {
        return players;
    }
}
