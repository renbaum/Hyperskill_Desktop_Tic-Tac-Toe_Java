package tictactoe;

import javax.swing.*;

public class PlayerButton extends JButton {
    int player;
    int type = 1;

    public PlayerButton(int player) {
        super("Human");
        this.player = player;
        setPlayerType();

        addActionListener(e -> {changePlayerType();});
    }

    private void setPlayerType(){
        switch(type){
            case 1:
                setText("Human");
                Board.getInstance().setPlayer("user", player - 1);
                break;
            case 2:
                setText("Robot");
                Board.getInstance().setPlayer("hard", player - 1);
                break;
        }
    }

    public void setPlayerType(int type){
        this.type = type;
        setPlayerType();
    }

    private void changePlayerType(){
        if(type == 1)
            type = 2;
        else
            type = 1;
        setPlayerType();
    }

}
