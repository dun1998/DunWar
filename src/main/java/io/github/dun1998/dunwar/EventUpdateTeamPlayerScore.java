package io.github.dun1998.dunwar;

import java.util.List;

public class EventUpdateTeamPlayerScore implements GameEvent{
    private WarTeam team;
    //for each player divide points
    private int addedScore;
    private String optionalSource;
    private static final int DEFAULT_SCORE = 10;

    public EventUpdateTeamPlayerScore(WarTeam team, int addedScore, String optionalSource){
        this.team = team;
        this.addedScore = Math.max(addedScore, 0);
        this.optionalSource = optionalSource;
    }

    public EventUpdateTeamPlayerScore(WarTeam team, int addedScore){
        this(team,addedScore,null);
    }

    public EventUpdateTeamPlayerScore(WarTeam team, String optionalSource){
        this(team,DEFAULT_SCORE,optionalSource);
    }
    public EventUpdateTeamPlayerScore(WarTeam team){
        this(team,DEFAULT_SCORE,null);
    }

    @Override
    public boolean runTask() {
        List<WarPlayer> teamPlayers = team.getPlayers();
        if(!teamPlayers.isEmpty()){
            int dividedScore = this.addedScore/teamPlayers.size();
            for(WarPlayer player:teamPlayers){
                player.addScore(dividedScore);
            }
        }
        return false;
    }
}
