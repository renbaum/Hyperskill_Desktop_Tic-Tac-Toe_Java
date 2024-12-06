package tictactoe;

import javax.swing.*;
import java.awt.event.ActionListener;

public class OwnButton extends JButton {
    int x, y;

    public OwnButton(int x, int y) {
        super("");
        this.x = x;
        this.y = y;
        String label = String.format("%c%d", 'A' + y, x+1);
        setText(" ");
        setName(String.format("Button%s", label));

        addActionListener(e -> {setText(String.format("%c", Board.getInstance().setStone(x, y)));});
    }



}
