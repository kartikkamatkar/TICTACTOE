package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Board> snapshots;
    private final List<String> moves;
    private int currentIndex;

    public GameHistory() {
        this.snapshots = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.currentIndex = -1;
    }

    public void addSnapshot(Board board, String moveDescription) {
        snapshots.add(board.copy());
        moves.add(moveDescription);
        currentIndex = snapshots.size() - 1;
    }

    public Board undo() {
        if (currentIndex <= 0) {
            return null;
        }
        currentIndex--;
        return snapshots.get(currentIndex);
    }

    public boolean canUndo() {
        return currentIndex > 0;
    }

    public List<String> getMoveHistory() {
        return new ArrayList<>(moves);
    }

    public void clear() {
        snapshots.clear();
        moves.clear();
        currentIndex = -1;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}