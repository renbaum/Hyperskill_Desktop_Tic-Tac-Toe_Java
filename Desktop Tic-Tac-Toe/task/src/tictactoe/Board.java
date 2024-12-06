package tictactoe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Board implements Cloneable{
    private char[][] board = new char[3][3];
    private int turn = 0;
    private boolean finished = false;
    static Board instance = null;
    JLabel label = null;
    private Player[] players;
    private TicTacToe gui = null;
    private List<CustomEventListener> listeners = new ArrayList<>();

    public static Board getInstance(){
        if(instance == null){
            instance = new Board();
        }
        return instance;
    }

    private Board() {
        reset();
        players = new Player[2];
    }

    public void addListener(CustomEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CustomEventListener listener) {
        listeners.remove(listener);
    }

    public void notifyCustomListeners() {
        if(gui == null) return;
        customEvent event = new customEvent(this);
        for(CustomEventListener listener : listeners) {
            listener.onMoveMade(event);
        }
    }

    public Board clone() {
        try {
            Board board = (Board) super.clone();
            board.board = new char[3][3];
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    board.board[i][j] = this.board[i][j];
                }
            }
            board.turn = this.turn;
            board.finished = this.finished;
            for(int i = 0; i < 2; i++){
                board.players[i] = this.players[i];
            }
            board.label = null;
            board.gui = null;
            return board;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void setPlayer(String type, int playerNumber) {
        if(playerNumber > 1 || playerNumber < 0) return;
        players[playerNumber] = FactoryPlayer.getPlayer(type);
    }
    
    public char setStone(int x, int y){
        if(board[x][y] != ' ' || finished){return board[x][y];}
        //if(gui != null) gui.disableBoard(true);
        
        char current = turn % 2 == 0 ? 'X' : 'O';

        //setMessage(String.format("Game in progress"));
        board[x][y] = current;

        if(checkWin(current)) {
            setMessage(String.format("The %s Player (%c) wins", players[turn % 2].getType(), current));
            finished = true;
        }
        turn++;
        if(turn == 9) {
            setMessage("Draw");
            finished = true;
        }
        notifyCustomListeners();

        return current;
    }

    
    public boolean setPosition(char player, int x, int y){
        if(!checkPosition(x, y)){
            return false;
        }
        setStone(x, y);
        //board[x][y] = player;
        return true;
    }


    
    
    
    public boolean checkWin(char player) {
        // Check rows and columns
        for(int i = 0; i < 3; i++) {
            if((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
               (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }

        // Check diagonals
        if((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
           (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }

        return false;
    }

    public void reset() {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = ' ';
            }
        }
        turn = 0;
        finished = false;
        setMessage("Game is not started");
    }

    public void setMessageLabel(JLabel labelStatus) {
        label = labelStatus;
        setMessage("Game is not started");
    }

    void setMessage(String message){
        if(label == null) return;
        label.setText(message);
    }

    private boolean checkPosition(int x, int y){
        if (board[x][y] == ' '){
            return true;
        }
        return false;
    }
    
    public List<Move> getMoves(){
        List<Move> moves = new ArrayList<Move>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(checkPosition(i, j)) moves.add(new Move(i, j));
            }
        }
        return moves;
    }


    public void setGUI(TicTacToe ticTacToe) {
        this.gui = ticTacToe;
    }

    public void enableBoard() {
        gui.disableBoard(false);
    }

    public void nextPlayer() {
        if(finished) return;
        gui.disableBoard(true);
        char current = turn % 2 == 0 ? 'X' : 'O';
        char opposite = current == 'X' ? 'O' : 'X';
        setMessage(String.format("The turn of %s Player (%c)", players[turn % 2].getType(), current));

        int[] move = players[turn % 2].getMove(this, current, opposite);
        if(move == null) return;
        gui.setStone(move[0], move[1], current);
        //setPosition(current, move[0], move[1]);
//        if(checkWin(current)) {
//            setMessage(String.format("%c wins", current));
//        }
    }
}

class Move{
    int x;
    int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

abstract class Player{
    String type = "Robot";

    public abstract int[] getMove(Board board, char thisPlayer, char oppositePlayer);

    public String getType() {return type;}
}

class HumanPlayer extends Player{
    public HumanPlayer() {
        super();
        type = "Human";
    }

    public int[] getMove(Board board, char thisPlayer, char oppositePlayer){
        Board.getInstance().enableBoard();
        return null;
    }

}

class ComputerEasy extends Player{
    public int[] getMove(Board board, char thisPlayer, char oppositePlayer){
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){

        }
        List<Move> moves = board.getMoves();
        int randomIndex = new Random().nextInt(moves.size());
        Move randomMove = moves.get(randomIndex);
        int[] move = {randomMove.x, randomMove.y};
        System.out.println("Making move level \"easy\"");
        return move;
    }

}

class ComputerMedium extends Player{
    private boolean checkForWinner(Board board, char symbol, int x, int y){
        Board dummy = board.clone();
        dummy.setPosition(symbol, x, y);
        return dummy.checkWin(symbol);
    }

    public int[] getMove(Board board, char thisPlayer, char oppositePlayer) {
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){

        }
        List<Move> moves = board.getMoves();
        List<Move> blockingMoves = new ArrayList<>();

        System.out.println("Making move level \"medium\"");

        for(Move m : moves){
            if(checkForWinner(board, thisPlayer, m.x, m.y)){
                int[] result = {m.x, m.y};
                return result;
            }
            if(checkForWinner(board, oppositePlayer, m.x, m.y)){
                blockingMoves.add(m);
            }
        }
        if(blockingMoves.size() != 0){
            Move bestMove = blockingMoves.get(0);
            int[] result = {bestMove.x, bestMove.y};
            return result;
        }

        int randomIndex = new Random().nextInt(moves.size());
        Move randomMove = moves.get(randomIndex);
        int[] move = {randomMove.x, randomMove.y};
        return move;
    }
}

class ComputerHard extends Player{
    public int[] getMove(Board board, char thisPlayer, char oppositePlayer) {
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){

        }
        int[] bestMove = minimax(board, thisPlayer, oppositePlayer);
        return bestMove;
    }

    public int[] minimax(Board board, char thisPlayer, char oppositePlayer) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(0, 0);

        List<Move> moves = board.getMoves();

        for (Move move : moves) {
            Board newBoard = board.clone();
            newBoard.setPosition(thisPlayer, move.x, move.y);
            int score = minimaxHelper(newBoard, 0, false, thisPlayer, oppositePlayer);
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
            }
        }
        int[] move = {bestMove.x, bestMove.y};
        return move;
    }

    private int minimaxHelper (Board board,int depth, boolean isMaximizing, char thisPlayer, char oppositePlayer)
    {
        if (board.checkWin(thisPlayer)) {
            return 10 - depth;
        } else if (board.checkWin(oppositePlayer)) {
            return depth - 10;
        } else if (board.getMoves().isEmpty()) {
            return 0;
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getMoves()) {
                Board newBoard = board.clone();
                newBoard.setPosition(thisPlayer, move.x, move.y);
                int eval = minimaxHelper(newBoard, depth + 1, false, thisPlayer, oppositePlayer);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getMoves()) {
                Board newBoard = board.clone();
                newBoard.setPosition(oppositePlayer, move.x, move.y);
                int eval = minimaxHelper(newBoard, depth + 1, true, thisPlayer, oppositePlayer);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }
}

class FactoryPlayer {

    static Player getPlayer(String type) {
        switch (type) {
            case "user":
                return new HumanPlayer();
            case "easy":
                return new ComputerEasy();
            case "medium":
                return new ComputerMedium();
            case "hard":
                return new ComputerHard();
        }
        return null;
    }
}
