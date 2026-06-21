import controller.GameController;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater for thread safety
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            controller.startGUI();
        });
    }
}