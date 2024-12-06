package tictactoe;

import java.util.EventListener;

public interface CustomEventListener extends EventListener {
    void onMoveMade(customEvent e);
}
