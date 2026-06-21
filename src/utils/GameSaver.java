package utils;

import model.Board;
import model.Player;
import model.GameHistory;
import controller.GameController;

import java.io.*;

public class GameSaver {
    private static final String SAVE_FILE = "tic_tac_toe_save.ser";

    public static void saveGame(Board board, Player player1, Player player2,
                                Player currentPlayer, GameHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE))) {
            GameSaveData data = new GameSaveData(board, player1, player2,
                    currentPlayer, history);
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game: " + e.getMessage());
        }
    }

    public static GameSaveData loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE))) {
            return (GameSaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load game: " + e.getMessage());
        }
    }

    public static boolean saveExists() {
        return new File(SAVE_FILE).exists();
    }

    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static class GameSaveData implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Board board;
        private final Player player1;
        private final Player player2;
        private final Player currentPlayer;
        private final GameHistory history;

        public GameSaveData(Board board, Player player1, Player player2,
                            Player currentPlayer, GameHistory history) {
            this.board = board;
            this.player1 = player1;
            this.player2 = player2;
            this.currentPlayer = currentPlayer;
            this.history = history;
        }

        public Board getBoard() { return board; }
        public Player getPlayer1() { return player1; }
        public Player getPlayer2() { return player2; }
        public Player getCurrentPlayer() { return currentPlayer; }
        public GameHistory getHistory() { return history; }
    }
}