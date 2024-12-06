package tictactoe;

import java.util.EventListener;
import java.util.EventObject;

public class customEvent extends EventObject {

    public customEvent(Object source) {
        super(source);
    }
    
}

