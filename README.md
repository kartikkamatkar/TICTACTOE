# Tic-Tac-Toe Game

A complete Java Swing Tic-Tac-Toe game featuring an AI opponent with multiple difficulty levels, MVC architecture, and advanced game management features.

---

## 🚀 Features

### Game Modes
* **Single Player:** Play against the computer with three AI difficulty levels:
  * **Easy:** Random moves.
  * **Medium:** Smart blocking and winning moves.
  * **Hard:** Minimax algorithm (perfect, unbeatable play).
* **Two Players:** Local multiplayer on the same computer.

### Game Features
* **Interactive Java Swing GUI:** Professional design with visual feedback.
* **Real-time Score Tracking:** Keeps track of Wins, Losses, and Draws.
* **Undo and Redo Functionality:** Full move history control.
* **Save and Load:** Persist your game state and resume later.
* **Visual Effects:** Winning cells highlighted in green, custom hover effects, and distinct colors (Red X, Blue O).
* **Player Statistics:** Tracks win percentages and historical records.

### Technical Highlights
* **MVC Architecture:** Strict separation of concerns (Model, View, Controller).
* **Object-Oriented Design & SOLID Principles:** Written with clean, maintainable, and extensible code.
* **Minimax AI Algorithm:** Advanced decision-tree logic for the Hard difficulty.
* **Serializable Save System:** Native Java serialization for game states.
* **Thread-safe GUI:** Updates handled correctly via `SwingUtilities`.

---

## 📁 Project Structure

```text
TicTacToe/
│
├── Main.java
├── controller/
│   └── GameController.java
├── model/
│   ├── Board.java
│   ├── Player.java
│   ├── GameState.java
│   ├── Symbol.java
│   ├── Difficulty.java
│   └── GameHistory.java
├── view/
│   └── GameGUI.java
├── ai/
│   └── AIPlayer.java
└── utils/
    └── GameSaver.java
