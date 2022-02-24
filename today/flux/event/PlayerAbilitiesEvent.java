package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;


public class PlayerAbilitiesEvent implements Event {
    
    EventType type;

    public PlayerAbilitiesEvent(EventType type) {
        this.type = type;
    }
}
