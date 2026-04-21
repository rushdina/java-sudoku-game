package sudoku;

import javax.swing.SwingUtilities;

/**
 * Entry point of the Sudoku application.
 */
public class SudokuMain {
    /** The entry main() entry method */
    public static void main(String[] args) {
        // Ensure UI is created on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
             new MainMenu();
        });
    }
}