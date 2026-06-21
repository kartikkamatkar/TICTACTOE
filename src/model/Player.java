package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Symbol symbol;
    private int wins;
    private int losses;
    private int draws;

    public Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public String getName() {
        return name;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public int getDraws() {
        return draws;
    }

    public void incrementDraws() {
        this.draws++;
    }

    public String getStats() {
        return String.format("%s: Wins=%d, Losses=%d, Draws=%d",
                name, wins, losses, draws);
    }
}