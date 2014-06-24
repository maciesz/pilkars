package main.gala.wifi;

import java.io.Serializable;

/**
 * Wiadomość z rozmiarami planszy - wysyła ją serwer (group owner) narzucając
 * je klientowi.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class BoardSizeWifiMessage implements Serializable {
    private static final long serialVersionUID = 124662L;

    private int boardWidth;
    private int boardHeight;
    private int goalSize;

    public BoardSizeWifiMessage(int boardWidth, int boardHeight, int goalSize) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.goalSize = goalSize;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public void setGoalSize(int goalSize) {
        this.goalSize = goalSize;
    }
}
