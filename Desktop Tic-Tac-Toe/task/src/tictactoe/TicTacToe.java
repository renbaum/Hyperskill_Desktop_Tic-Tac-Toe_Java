package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class TicTacToe extends JFrame implements CustomEventListener{

    OwnButton[][] buttons = new OwnButton[3][3];
    JPanel board = new JPanel();
    JPanel status = new JPanel();
    JPanel btn = new JPanel();
    JLabel labelStatus = new JLabel("Game is not started");
    JButton buttonReset = new JButton("Start");
    PlayerButton playerOne = new PlayerButton(1);
    PlayerButton playerTwo = new PlayerButton(2);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Game");
    JMenuItem itemHH = new JMenuItem("Human vs Human");
    JMenuItem itemHR = new JMenuItem("Human vs Robot");
    JMenuItem itemRH = new JMenuItem("Robot vs Human");
    JMenuItem itemRR = new JMenuItem("Robot vs Robot");
    JMenuItem itemExit = new JMenuItem("Exit");

    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Tic Tac Toe");
        Board.getInstance().setMessageLabel(labelStatus);
        Board.getInstance().setGUI(this);
        Board.getInstance().addListener(this);

        menu.add(itemHH);
        menu.add(itemHR);
        menu.add(itemRH);
        menu.add(itemRR);
        menu.addSeparator();
        menu.add(itemExit);
        menuBar.add(menu);

        menu.setName("MenuGame");
        itemHH.setName("MenuHumanHuman");
        itemHR.setName("MenuHumanRobot");
        itemRH.setName("MenuRobotHuman");
        itemRR.setName("MenuRobotRobot");
        itemExit.setName("MenuExit");
        setJMenuBar(menuBar);


        itemHH.addActionListener(e -> {
            playerOne.setPlayerType(1);
            playerTwo.setPlayerType(1);
            startGame();
        });
        itemHR.addActionListener(e -> {
            playerOne.setPlayerType(1);
            playerTwo.setPlayerType(2);
            startGame();
        });
        itemRH.addActionListener(e -> {
            playerOne.setPlayerType(2);
            playerTwo.setPlayerType(1);
            startGame();
        });
        itemRR.addActionListener(e -> {
            playerOne.setPlayerType(2);
            playerTwo.setPlayerType(2);
            startGame();
        });
        itemExit.addActionListener(e -> {System.exit(0);});

        setLayout(new BorderLayout());
        board.setLayout(new GridLayout(3, 3));
        status.setLayout(new FlowLayout());
        btn.setLayout(new GridLayout(1, 3));
        status.add(labelStatus);
        buttonReset.setName("ButtonStartReset");
        labelStatus.setName("LabelStatus");
        playerOne.setName("ButtonPlayer1");
        playerTwo.setName("ButtonPlayer2");
        btn.add(playerOne);
        btn.add(buttonReset);
        btn.add(playerTwo);
        for(int i = 2; i >=0; i--){
            for(int j = 0; j < 3; j++){
                buttons[i][j] = new OwnButton(i, j);
                board.add(buttons[i][j]);
            }
        }

        disableBoard(true);
        disablePlayer(false);

        add(board, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        add(btn, BorderLayout.NORTH);
        buttonReset.addActionListener(e -> {
            if(buttonReset.getText().equals("Reset")){
                disablePlayer(false);
                reset();
                buttonReset.setText("Start");
                labelStatus.setText("Game is not started");
            }else{
                startGame();
                //labelStatus.setText("Game in progress");
            };});

        
        setVisible(true);
    }

    private void startGame(){
        disablePlayer(true);
        Board.getInstance().nextPlayer();
        buttonReset.setText("Reset");
    }

    private void disablePlayer(boolean b) {
        playerOne.setEnabled(!b);
        playerTwo.setEnabled(!b);

//        itemHH.setEnabled(!b);
//        itemHR.setEnabled(!b);
//        itemRH.setEnabled(!b);
//        itemRR.setEnabled(!b);
    }

    void setStone(int x, int y, char stone) {
        buttons[x][y].setText(String.format("%c", Board.getInstance().setStone(x, y)));
    }
    
    
    private void reset() {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText(" ");
            }
        }
        Board.getInstance().reset();
        labelStatus.setText("Game is not started");
    }

    public void disableBoard(boolean b) {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setEnabled(!b);
            }
        }
    }

    @Override
    public void onMoveMade(customEvent e) {
        Board.getInstance().nextPlayer();
    }
}
