import controller.GameController;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            controller.startGUI();
        });
    }
}