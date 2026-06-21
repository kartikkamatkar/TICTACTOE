package ai;

import model.Board;
import model.Difficulty;
import model.Symbol;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private final Symbol aiSymbol;
    private final Difficulty difficulty;
    private final Random random;

    public AIPlayer(Symbol aiSymbol, Difficulty difficulty) {
        this.aiSymbol = aiSymbol;
        this.difficulty = difficulty;
        this.random = new Random();
    }

    public int[] getMove(Board board) {
        switch (difficulty) {
            case EASY:
                return getRandomMove(board);
            case MEDIUM:
                return getMediumMove(board);
            case HARD:
                return getBestMove(board);
            default:
                return getRandomMove(board);
        }
    }

    private int[] getRandomMove(Board board) {
        List<int[]> available = getAvailableMoves(board);
        if (available.isEmpty()) {
            return null;
        }
        return available.get(random.nextInt(available.size()));
    }

    private int[] getMediumMove(Board board) {
        List<int[]> available = getAvailableMoves(board);
        if (available.isEmpty()) {
            return null;
        }

        for (int[] move : available) {
            Board testBoard = board.copy();
            testBoard.placeSymbol(move[0], move[1], aiSymbol);
            if (testBoard.checkWinner() == aiSymbol) {
                return move;
            }
        }

        Symbol opponent = aiSymbol.getOpposite();
        for (int[] move : available) {
            Board testBoard = board.copy();
            testBoard.placeSymbol(move[0], move[1], opponent);
            if (testBoard.checkWinner() == opponent) {
                return move;
            }
        }

        return available.get(random.nextInt(available.size()));
    }

    private int[] getBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : getAvailableMoves(board)) {
            Board testBoard = board.copy();
            testBoard.placeSymbol(move[0], move[1], aiSymbol);
            int score = minimax(testBoard, 0, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        Symbol winner = board.checkWinner();
        if (winner == aiSymbol) {
            return 10 - depth;
        }
        if (winner == aiSymbol.getOpposite()) {
            return depth - 10;
        }
        if (board.isFull()) {
            return 0;
        }

        List<int[]> available = getAvailableMoves(board);

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int[] move : available) {
                Board testBoard = board.copy();
                testBoard.placeSymbol(move[0], move[1], aiSymbol);
                int score = minimax(testBoard, depth + 1, false);
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int[] move : available) {
                Board testBoard = board.copy();
                testBoard.placeSymbol(move[0], move[1], aiSymbol.getOpposite());
                int score = minimax(testBoard, depth + 1, true);
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }
    }

    private List<int[]> getAvailableMoves(Board board) {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isEmpty(row, col)) {
                    moves.add(new int[]{row, col});
                }
            }
        }
        return moves;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}