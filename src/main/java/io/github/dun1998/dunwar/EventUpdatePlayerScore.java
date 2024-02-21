package io.github.dun1998.dunwar;

import org.jetbrains.annotations.NotNull;

public class EventUpdatePlayerScore implements GameEvent{

    @NotNull private WarPlayer targetPlayer;
    private int addedScore;
    private String optionalSource;
    private static final int DEFAULT_SCORE = 10;

    public EventUpdatePlayerScore(WarPlayer targetPlayer,int addedScore,String optionalSource){
        this.targetPlayer = targetPlayer;
        this.addedScore = Math.max(addedScore, 0);
        this.optionalSource = optionalSource;
    }

    public EventUpdatePlayerScore(WarPlayer targetPlayer,int addedScore){
        this(targetPlayer,addedScore,null);
    }
    public EventUpdatePlayerScore(WarPlayer targetPlayer,String optionalSource){
        this(targetPlayer,DEFAULT_SCORE,optionalSource);
    }
    public EventUpdatePlayerScore(WarPlayer targetPlayer) {
        this(targetPlayer,DEFAULT_SCORE,null);
    }

    @Override
    public boolean runTask() {
        this.targetPlayer.addScore(this.addedScore);
        return false;
    }
}
