package io.github.dun1998.dunwar;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager {
    private List<GameEvent> eventQueue = new ArrayList<>();


    public void queueTask(GameEvent event){
        eventQueue.add(event);
    }
    public void runTasks(){
        while(!eventQueue.isEmpty()){
            GameEvent event = eventQueue.removeFirst();
            event.runTask();
        }
    }

    public boolean hasTasks(){
        return !eventQueue.isEmpty();
    }

}
