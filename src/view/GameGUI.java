package view;

import model.Board;
import model.Symbol;
import model.Player;
import model.Difficulty;
import controller.GameController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JButton[][] cellButtons;
    private JLabel statusLabel;
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel scoreLabel;
    private JButton undoButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton newGameButton;
    private JButton menuButton;

    private GameController controller;
    private Board board;
    private Player currentPlayer;
    private boolean isAIMode;
    private boolean isGameOver;

    public GameGUI(GameController controller) {
        this.controller = controller;
        this.board = new Board();
        this.cellButtons = new JButton[3][3];
        this.isGameOver = false;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        createHeaderPanel();
        createBoardPanel();
        createControlPanel();
        createStatusPanel();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);

        player1Label = new JLabel("Player 1 (X)");
        player1Label.setFont(new Font("Arial", Font.BOLD, 16));
        player1Label.setForeground(Color.RED);
        headerPanel.add(player1Label, gbc);

        gbc.gridx = 1;
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        vsLabel.setForeground(new Color(100, 100, 100));
        headerPanel.add(vsLabel, gbc);

        gbc.gridx = 2;
        player2Label = new JLabel("Player 2 (O)");
        player2Label.setFont(new Font("Arial", Font.BOLD, 16));
        player2Label.setForeground(Color.BLUE);
        headerPanel.add(player2Label, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        boardPanel.setBackground(new Color(240, 248, 255));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 60));
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

                final int r = row;
                final int c = col;
                button.addActionListener(e -> handleCellClick(r, c));

                cellButtons[row][col] = button;
                boardPanel.add(button);
            }
        }

        // Add hover effect
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row;
                final int c = col;
                cellButtons[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!isGameOver && board.isEmpty(r, c) &&
                                !(isAIMode && currentPlayer != controller.getPlayer1())) {
                            cellButtons[r][c].setBackground(new Color(220, 240, 255));
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!isGameOver) {
                            cellButtons[r][c].setBackground(Color.WHITE);
                        }
                    }
                });
            }
        }

        mainPanel.add(boardPanel, BorderLayout.CENTER);
    }

    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.setBackground(new Color(240, 248, 255));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        undoButton = createStyledButton("↩ Undo", new Color(255, 165, 0));
        saveButton = createStyledButton("💾 Save", new Color(0, 150, 0));
        loadButton = createStyledButton("📂 Load", new Color(0, 100, 200));
        newGameButton = createStyledButton("🔄 New Game", new Color(150, 0, 150));
        menuButton = createStyledButton("📋 Menu", new Color(100, 100, 100));

        undoButton.addActionListener(e -> handleUndo());
        saveButton.addActionListener(e -> handleSave());
        loadButton.addActionListener(e -> handleLoad());
        newGameButton.addActionListener(e -> handleNewGame());
        menuButton.addActionListener(e -> handleMenu());

        controlPanel.add(undoButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(newGameButton);
        controlPanel.add(menuButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(240, 248, 255));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        statusLabel = new JLabel("Welcome to Tic-Tac-Toe!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(new Color(50, 50, 50));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreLabel.setForeground(new Color(100, 100, 100));
        statusPanel.add(scoreLabel, BorderLayout.SOUTH);

        mainPanel.add(statusPanel, BorderLayout.NORTH);
    }

    private void handleCellClick(int row, int col) {
        if (isGameOver) {
            return;
        }

        if (isAIMode && currentPlayer != controller.getPlayer1()) {
            return;
        }

        if (!board.isEmpty(row, col)) {
            showMessage("Position already occupied!", "Invalid Move", JOptionPane.WARNING_MESSAGE);
            return;
        }

        makeMove(row, col);
    }

    public void makeMove(int row, int col) {
        if (board.placeSymbol(row, col, currentPlayer.getSymbol())) {
            updateBoard();

            if (checkGameEnd()) {
                return;
            }

            switchTurn();

            if (isAIMode && currentPlayer == controller.getPlayer2()) {
                performAIMove();
            }
        }
    }

    private void performAIMove() {
        statusLabel.setText("Computer is thinking...");
        statusLabel.setForeground(new Color(100, 100, 100));

        Timer timer = new Timer(500, e -> {
            if (isGameOver) {
                return;
            }

            int[] aiMove = controller.getAIMove();
            if (aiMove != null) {
                board.placeSymbol(aiMove[0], aiMove[1], currentPlayer.getSymbol());
                updateBoard();
                checkGameEnd();
                if (!isGameOver) {
                    switchTurn();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private boolean checkGameEnd() {
        Symbol winner = board.checkWinner();
        if (winner != null) {
            isGameOver = true;
            Player winnerPlayer = (winner == controller.getPlayer1().getSymbol()) ?
                    controller.getPlayer1() : controller.getPlayer2();
            controller.handleWin(winnerPlayer);
            showWinner(winnerPlayer);
            updateScoreDisplay();
            return true;
        } else if (board.isFull()) {
            isGameOver = true;
            controller.handleDraw();
            showDraw();
            updateScoreDisplay();
            return true;
        }
        return false;
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == controller.getPlayer1()) ?
                controller.getPlayer2() : controller.getPlayer1();
        updateStatus();
    }

    private void updateBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Symbol symbol = board.getSymbolAt(row, col);
                JButton button = cellButtons[row][col];

                if (symbol == Symbol.X) {
                    button.setText("X");
                    button.setForeground(Color.RED);
                    button.setBackground(Color.WHITE);
                } else if (symbol == Symbol.O) {
                    button.setText("O");
                    button.setForeground(Color.BLUE);
                    button.setBackground(Color.WHITE);
                } else {
                    button.setText("");
                    button.setBackground(Color.WHITE);
                }
            }
        }
    }

    public void updateStatus() {
        if (!isGameOver) {
            String playerName = currentPlayer.getName();
            String symbol = currentPlayer.getSymbol().getDisplayChar() + "";
            String color = (currentPlayer.getSymbol() == Symbol.X) ? "RED" : "BLUE";
            statusLabel.setText("<html><font color='" + color + "'>" + playerName +
                    "</font> (" + symbol + ")'s turn</html>");
            statusLabel.setForeground(Color.BLACK);

            // Highlight current player
            if (currentPlayer == controller.getPlayer1()) {
                player1Label.setForeground(Color.RED);
                player2Label.setForeground(Color.BLUE);
            } else {
                player1Label.setForeground(Color.RED);
                player2Label.setForeground(Color.BLUE);
            }
        }
    }

    public void updateScoreDisplay() {
        Player p1 = controller.getPlayer1();
        Player p2 = controller.getPlayer2();
        scoreLabel.setText(String.format("%s: %d wins | %s: %d wins | Draws: %d",
                p1.getName(), p1.getWins(), p2.getName(), p2.getWins(), p1.getDraws()));
    }

    public void showWinner(Player player) {
        String symbol = player.getSymbol().getDisplayChar() + "";
        String color = (player.getSymbol() == Symbol.X) ? "RED" : "BLUE";
        statusLabel.setText("<html><font color='" + color + "' size='+2'>🏆 " +
                player.getName() + " (" + symbol + ") wins!</font></html>");

        // Highlight winning cells
        highlightWinningCells();
    }

    private void highlightWinningCells() {
        // Find winning combination and highlight
        for (int i = 0; i < 3; i++) {
            if (checkAndHighlight(i, 0, i, 1, i, 2)) return;
            if (checkAndHighlight(0, i, 1, i, 2, i)) return;
        }
        if (checkAndHighlight(0, 0, 1, 1, 2, 2)) return;
        if (checkAndHighlight(0, 2, 1, 1, 2, 0)) return;
    }

    private boolean checkAndHighlight(int r1, int c1, int r2, int c2, int r3, int c3) {
        if (board.getSymbolAt(r1, c1) != Symbol.EMPTY &&
                board.getSymbolAt(r1, c1) == board.getSymbolAt(r2, c2) &&
                board.getSymbolAt(r2, c2) == board.getSymbolAt(r3, c3)) {
            highlightCell(r1, c1);
            highlightCell(r2, c2);
            highlightCell(r3, c3);
            return true;
        }
        return false;
    }

    private void highlightCell(int row, int col) {
        cellButtons[row][col].setBackground(new Color(144, 238, 144));
        cellButtons[row][col].setBorder(BorderFactory.createLineBorder(new Color(0, 150, 0), 3));
    }

    public void showDraw() {
        statusLabel.setText("<html><font color='ORANGE' size='+2'>🤝 It's a draw!</font></html>");
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }

    private void handleUndo() {
        if (isGameOver) {
            showMessage("Game is over. Start a new game to undo.", "Cannot Undo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        controller.undoMove();
        updateBoard();
        updateStatus();
        updateScoreDisplay();
    }

    private void handleSave() {
        try {
            controller.saveGame();
            showMessage("Game saved successfully!", "Save Game",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showMessage("Failed to save game: " + e.getMessage(), "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoad() {
        try {
            controller.loadGame();
            updateBoard();
            updateStatus();
            updateScoreDisplay();
            showMessage("Game loaded successfully!", "Load Game",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showMessage("Failed to load game: " + e.getMessage(), "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleNewGame() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Start a new game? Current game progress will be lost.",
                "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.resetGame();
            board = controller.getBoard();
            currentPlayer = controller.getCurrentPlayer();
            isGameOver = false;
            updateBoard();
            updateStatus();
            updateScoreDisplay();
            resetCellBorders();
        }
    }

    private void handleMenu() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Return to main menu? Current game progress will be lost.",
                "Main Menu", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            controller.showMainMenu();
        }
    }

    private void resetCellBorders() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cellButtons[row][col].setBorder(BorderFactory.createLineBorder(
                        new Color(100, 100, 100), 2));
                cellButtons[row][col].setBackground(Color.WHITE);
            }
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setAIMode(boolean isAIMode) {
        this.isAIMode = isAIMode;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void updateAll() {
        updateBoard();
        updateStatus();
        updateScoreDisplay();
        resetCellBorders();
    }

    public void setPlayerNames(String player1Name, String player2Name) {
        player1Label.setText(player1Name + " (X)");
        player2Label.setText(player2Name + " (O)");
    }

    public void dispose() {
        frame.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }
}