package sudoku;

// import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameBoard extends JPanel {
    // Game board properties
    public static final int GRID_SIZE = 9; // size of the board
    public static final int SUBGRID_SIZE = 3; // size of the sub-grid

    // UI sizes
    public static final int CELL_SIZE = 60; // cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * GRID_SIZE;

    // Sound effect played when the puzzle is completed
    private static final String CONGRATS_SOUND_FILE = "assets/sounds/congrats.wav";

    // Gameboard composes of 9x9 "Customized" JTextFields,
    private final Cell[][] cells = new Cell[GRID_SIZE][GRID_SIZE];
    private final Puzzle puzzle = new Puzzle();
    private final AudioManager audioManager = new AudioManager();
    private final Clip congratsClip; // Java Internal Sound Clip

    // Callback used by SudokuFrame to handle actions when the puzzle is solved
    private Runnable onPuzzleSolved;

    public Clip getCongratsClip() {
        return congratsClip;
    }

    public void setOnPuzzleSolved(Runnable onPuzzleSolved) {
        this.onPuzzleSolved = onPuzzleSolved;
    }

    // Constructor
    public GameBoard() {
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        // Allocate common listener as ActionEvent listener for all Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // Allocate 2D array of Cell, and added into JPanel, editable cell with common
        // listener
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].addActionListener(listener);
                add(cells[row][col]);
            }
        }

        congratsClip = audioManager.loadClip(CONGRATS_SOUND_FILE);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    // Puzzle 1
    public void init() {
        puzzle.newPuzzle(9); // get a new puzzle
        initializeCells();
        applyGame1Theme();
    }

    // Puzzle 2
    public void init2() {
        puzzle.newPuzzle2(14);
        initializeCells();
        applyGame2Theme();
    }

    // Loads puzzle values and visibility state into each cell
    private void initializeCells() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].init(puzzle.getNumbers()[row][col], puzzle.getIsShown()[row][col]);
            }
        }
    }

    private void applyGame1Theme() {
        paintAllCellsGame1Base();
        paintHighlightedSubgridsGame1();
    }

    private void applyGame2Theme() {
        paintAllCellsGame2Base();
        paintHighlightedSubgridsGame2();
    }

    // Assign base theme for all cells first, then overwrite selected 3x3 blocks
    // with accent theme
    private void paintAllCellsGame1Base() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setThemeType(2);
                cells[row][col].paint2(); // pastel purple
            }
        }
    }

    private void paintAllCellsGame2Base() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setThemeType(3);
                cells[row][col].paint3(); // dark purple
            }
        }
    }

    private void paintHighlightedSubgridsGame1() {
        paintSubgridGame1(0, 0);
        paintSubgridGame1(0, 6);
        paintSubgridGame1(3, 3);
        paintSubgridGame1(6, 0);
        paintSubgridGame1(6, 6);
    }

    private void paintHighlightedSubgridsGame2() {
        paintSubgridGame2(0, 0);
        paintSubgridGame2(0, 6);
        paintSubgridGame2(3, 3);
        paintSubgridGame2(6, 0);
        paintSubgridGame2(6, 6);
    }

    private void paintSubgridGame1(int startRow, int startCol) {
        for (int row = startRow; row < startRow + SUBGRID_SIZE; row++) {
            for (int col = startCol; col < startCol + SUBGRID_SIZE; col++) {
                cells[row][col].setThemeType(1);
                cells[row][col].paint1(); // pastel blue
            }
        }
    }

    private void paintSubgridGame2(int startRow, int startCol) {
        for (int row = startRow; row < startRow + SUBGRID_SIZE; row++) {
            for (int col = startCol; col < startCol + SUBGRID_SIZE; col++) {
                cells[row][col].setThemeType(4);
                cells[row][col].paint4(); // dark magenta
            }
        }
    }

    // Puzzle is solved only when no cell still needs a guess
    public boolean isSolved() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getStatus().needsGuess()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Handles Enter key submission for editable cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell) e.getSource();
            String input = sourceCell.getText().trim();

            if (input.isEmpty()) {
                sourceCell.setStatus(CellStatus.NO_GUESS);
                sourceCell.repaintByTheme();
                return;
            }

            if (!input.matches("[1-9]")) {
                JOptionPane.showMessageDialog(
                        GameBoard.this,
                        "Please enter a number from 1 to 9.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
                sourceCell.setText("");
                sourceCell.setStatus(CellStatus.NO_GUESS);
                sourceCell.repaintByTheme();
                return;
            }

            int numberIn = Integer.parseInt(input); // retrieve int entered
            System.out.println("You entered " + numberIn);

            /*
             * Check the numberIn against sourceCell.number.
             * Update the cell status sourceCell.status, and re-paint the cell via
             * sourceCell.paint().
             */
            if (numberIn == sourceCell.getNumber()) {
                sourceCell.setStatus(CellStatus.CORRECT_GUESS);
            } else {
                sourceCell.setStatus(CellStatus.WRONG_GUESS);
            }

            sourceCell.repaintByTheme();

            if (isSolved()) {
                if (onPuzzleSolved != null) {
                    onPuzzleSolved.run();
                } else {
                    audioManager.playOnce(congratsClip);
                    JOptionPane.showMessageDialog(GameBoard.this, "Congratulations!");
                }
            }
        }
    }
}
