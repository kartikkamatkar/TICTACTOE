package controller;

import model.*;
import view.GameGUI;
import ai.AIPlayer;
import utils.GameSaver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameController {
    private GameGUI gui;
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GameHistory history;
    private GameState gameState;
    private boolean isAIMode;
    private AIPlayer aiPlayer;
    private Difficulty currentDifficulty;

    public GameController() {
        this.board = new Board();
        this.history = new GameHistory();
        this.gameState = GameState.PLAYING;
        this.isAIMode = false;
        initializePlayers();
    }

    private void initializePlayers() {
        this.player1 = new Player("Player 1", Symbol.X);
        this.player2 = new Player("Player 2", Symbol.O);
        this.currentPlayer = player1;
    }

    public void startGUI() {
        showMainMenu();
    }

    public void showMainMenu() {
        JFrame menuFrame = new JFrame("Tic-Tac-Toe - Main Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 500);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setResizable(false);

        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(240, 248, 255));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        // Title
        JLabel titleLabel = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(50, 50, 150));
        menuPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Choose your game mode", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        menuPanel.add(subtitleLabel, gbc);

        // Spacer
        menuPanel.add(Box.createVerticalStrut(20), gbc);

        // Buttons
        JButton singlePlayerBtn = createMenuButton("🎮 Single Player", new Color(0, 150, 0));
        JButton twoPlayerBtn = createMenuButton("👥 Two Players", new Color(0, 100, 200));
        JButton statsBtn = createMenuButton("📊 Statistics", new Color(200, 150, 0));
        JButton exitBtn = createMenuButton("🚪 Exit", new Color(200, 50, 50));

        singlePlayerBtn.addActionListener(e -> {
            menuFrame.dispose();
            startSinglePlayer();
        });

        twoPlayerBtn.addActionListener(e -> {
            menuFrame.dispose();
            startTwoPlayer();
        });

        statsBtn.addActionListener(e -> showStatistics());

        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(singlePlayerBtn, gbc);
        menuPanel.add(twoPlayerBtn, gbc);
        menuPanel.add(statsBtn, gbc);
        menuPanel.add(exitBtn, gbc);

        // Footer
        JLabel footerLabel = new JLabel("Tic-Tac-Toe Game", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 150, 150));
        menuPanel.add(footerLabel, gbc);

        menuFrame.add(menuPanel);
        menuFrame.setVisible(true);
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void startSinglePlayer() {
        // Show difficulty selection dialog
        Difficulty[] difficulties = Difficulty.values();
        String[] diffNames = new String[difficulties.length];
        for (int i = 0; i < difficulties.length; i++) {
            diffNames[i] = difficulties[i].getDisplayName();
        }

        String selected = (String) JOptionPane.showInputDialog(null,
                "Select AI difficulty:",
                "Difficulty Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                diffNames,
                diffNames[0]);

        if (selected == null) {
            showMainMenu();
            return;
        }

        // Map selected to Difficulty enum
        for (Difficulty diff : difficulties) {
            if (diff.getDisplayName().equals(selected)) {
                currentDifficulty = diff;
                break;
            }
        }

        // Get player name
        String playerName = JOptionPane.showInputDialog(null,
                "Enter your name:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }

        isAIMode = true;
        player1 = new Player(playerName.trim(), Symbol.X);
        player2 = new Player("Computer", Symbol.O);
        aiPlayer = new AIPlayer(Symbol.O, currentDifficulty);
        currentPlayer = player1;

        resetGame();
        createAndShowGUI();
        gui.setAIMode(true);
        gui.setPlayerNames(player1.getName(), player2.getName());
        gui.updateAll();
    }

    private void startTwoPlayer() {
        // Get player names
        String player1Name = JOptionPane.showInputDialog(null,
                "Enter Player 1 name (X):",
                "Player 1 Name",
                JOptionPane.QUESTION_MESSAGE);

        if (player1Name == null || player1Name.trim().isEmpty()) {
            player1Name = "Player 1";
        }

        String player2Name = JOptionPane.showInputDialog(null,
                "Enter Player 2 name (O):",
                "Player 2 Name",
                JOptionPane.QUESTION_MESSAGE);

        if (player2Name == null || player2Name.trim().isEmpty()) {
            player2Name = "Player 2";
        }

        isAIMode = false;
        player1 = new Player(player1Name.trim(), Symbol.X);
        player2 = new Player(player2Name.trim(), Symbol.O);
        currentPlayer = player1;

        resetGame();
        createAndShowGUI();
        gui.setAIMode(false);
        gui.setPlayerNames(player1.getName(), player2.getName());
        gui.updateAll();
    }

    private void createAndShowGUI() {
        if (gui == null || !gui.getFrame().isVisible()) {
            gui = new GameGUI(this);
        } else {
            gui.setBoard(board);
            gui.setCurrentPlayer(currentPlayer);
            gui.setGameOver(false);
            gui.updateAll();
        }
    }

    private void showStatistics() {
        String stats = String.format(
                "═══════ STATISTICS ═══════\n" +
                        "%s (X): Wins=%d, Losses=%d, Draws=%d\n" +
                        "%s (O): Wins=%d, Losses=%d, Draws=%d\n" +
                        "═══════════════════════════",
                player1.getName(), player1.getWins(), player1.getLosses(), player1.getDraws(),
                player2.getName(), player2.getWins(), player2.getLosses(), player2.getDraws()
        );
        JOptionPane.showMessageDialog(null, stats, "Statistics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void handleCellClick(int row, int col) {
        if (gui != null) {
            gui.makeMove(row, col);
        }
    }

    public void makeMove(int row, int col) {
        if (board.placeSymbol(row, col, currentPlayer.getSymbol())) {
            history.addSnapshot(board, String.format("%s placed at (%d,%d)",
                    currentPlayer.getName(), row + 1, col + 1));

            if (checkGameEnd()) {
                return;
            }

            switchTurn();

            if (isAIMode && currentPlayer == player2 && gameState == GameState.PLAYING) {
                performAIMove();
            }
        }
    }

    private void performAIMove() {
        int[] move = aiPlayer.getMove(board);
        if (move != null) {
            board.placeSymbol(move[0], move[1], currentPlayer.getSymbol());
            history.addSnapshot(board, "Computer placed at (" + (move[0] + 1) + "," +
                    (move[1] + 1) + ")");

            checkGameEnd();
            if (gameState == GameState.PLAYING) {
                switchTurn();
            }

            if (gui != null) {
                gui.updateAll();
            }
        }
    }

    private boolean checkGameEnd() {
        Symbol winner = board.checkWinner();
        if (winner != null) {
            gameState = GameState.WINNER;
            if (winner == player1.getSymbol()) {
                player1.incrementWins();
                player2.incrementLosses();
                if (gui != null) {
                    gui.showWinner(player1);
                }
            } else {
                player2.incrementWins();
                player1.incrementLosses();
                if (gui != null) {
                    gui.showWinner(player2);
                }
            }
            if (gui != null) {
                gui.setGameOver(true);
                gui.updateScoreDisplay();
            }
            return true;
        } else if (board.isFull()) {
            gameState = GameState.DRAW;
            player1.incrementDraws();
            player2.incrementDraws();
            if (gui != null) {
                gui.showDraw();
                gui.setGameOver(true);
                gui.updateScoreDisplay();
            }
            return true;
        }
        return false;
    }

    public void handleWin(Player winner) {
        if (winner == player1) {
            player1.incrementWins();
            player2.incrementLosses();
        } else {
            player2.incrementWins();
            player1.incrementLosses();
        }
        gameState = GameState.WINNER;
        if (gui != null) {
            gui.setGameOver(true);
            gui.updateScoreDisplay();
        }
    }

    public void handleDraw() {
        player1.incrementDraws();
        player2.incrementDraws();
        gameState = GameState.DRAW;
        if (gui != null) {
            gui.setGameOver(true);
            gui.updateScoreDisplay();
        }
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        if (gui != null) {
            gui.setCurrentPlayer(currentPlayer);
            gui.updateStatus();
        }
    }

    public void undoMove() {
        if (history.canUndo()) {
            Board previousBoard = history.undo();
            if (previousBoard != null) {
                this.board = previousBoard;
                gameState = GameState.PLAYING;
                if (gui != null) {
                    gui.setGameOver(false);
                    gui.setBoard(board);
                    gui.updateAll();
                }
                switchTurn();
            }
        }
    }

    public void saveGame() {
        GameSaver.saveGame(board, player1, player2, currentPlayer, history);
    }

    public void loadGame() {
        GameSaver.GameSaveData data = GameSaver.loadGame();
        this.board = data.getBoard();
        this.player1 = data.getPlayer1();
        this.player2 = data.getPlayer2();
        this.currentPlayer = data.getCurrentPlayer();
        this.history = data.getHistory();
        this.gameState = GameState.PLAYING;

        if (gui != null) {
            gui.setBoard(board);
            gui.setCurrentPlayer(currentPlayer);
            gui.setGameOver(false);
            gui.updateAll();
        }
    }

    public void resetGame() {
        board.reset();
        history.clear();
        gameState = GameState.PLAYING;
        currentPlayer = player1;
        if (gui != null) {
            gui.setBoard(board);
            gui.setCurrentPlayer(currentPlayer);
            gui.setGameOver(false);
            gui.updateAll();
        }
    }

    public int[] getAIMove() {
        return aiPlayer.getMove(board);
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isAIMode() {
        return isAIMode;
    }

    public void setGUI(GameGUI gui) {
        this.gui = gui;
    }
}