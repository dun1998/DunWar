package io.github.dun1998.dunwar;


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
    Location spawnPoint;
    int id;
    NamedTextColor textColor;
    List<WarPlayer> players = new ArrayList<>();
    Map<String, NamedTextColor> colorConversion = new HashMap<String, NamedTextColor>()
    {{
        put("red", NamedTextColor.RED);
        put("blue", NamedTextColor.BLUE);
        put("purple", NamedTextColor.LIGHT_PURPLE);
        put("green", NamedTextColor.GREEN);
        put("yellow", NamedTextColor.YELLOW);
        put("orange", NamedTextColor.GOLD);
    }};
    public WarTeam(String color,int id){
        this.color = color.toLowerCase();
        this.name = color;
        this.id =id;
        if(colorConversion.containsKey(this.color)){
            this.textColor = colorConversion.get(this.color);
        }
    }

    public void AddPlayer(WarPlayer player){
        players.add((player));
    }
    public void RemovePlayer(WarPlayer player){
        if(players.contains(player)){
            players.remove(player);
        }
    }

    public void GetTeamInfo(CommandSender sender){
        //sends the player all the team info
        if(sender instanceof Player){
            sender.sendMessage(String.format("~~~~~~~~~~~~~~~~~~\nTeam: %s \nColor: %s",this.name,this.color));
        }
    }

}
