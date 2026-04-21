package sudoku;

import java.util.Random;

/**
 * The Sudoku number puzzle to be solved.
 * Each row, column, and 3x3 sub-grid must contain digits 1 to 9 exactly once.
 */
public class Puzzle {

    // Stores the full solved puzzle
	private final int[][] numbers = new int[GameBoard.GRID_SIZE][GameBoard.GRID_SIZE];

    // Indicates which cells are initially revealed (true = shown, false = hidden)
    private final boolean[][] isShown = new boolean[GameBoard.GRID_SIZE][GameBoard.GRID_SIZE]; 

	private final Random random = new Random();

    public int[][] getNumbers() {
        return numbers;
    }

    public boolean[][] getIsShown() {
        return isShown;
    }

	// Loads predefined Puzzle 1 and hides some cells based on difficulty
    public void newPuzzle(int numToGuess) {
        int[][] base = {
            {5,3,4,6,7,8,9,1,2},
            {6,7,2,1,9,5,3,4,8},
            {1,9,8,3,4,2,5,6,7},
            {8,5,9,7,6,1,4,2,3},
            {4,2,6,8,5,3,7,9,1},
            {7,1,3,9,2,4,8,5,6},
            {9,6,1,5,3,7,2,8,4},
            {2,8,7,4,1,9,6,3,5},
            {3,4,5,2,8,6,1,7,9}
        };

        loadPuzzle(base, numToGuess);
    }

    // Loads predefined Puzzle 2 and hides some cells based on difficulty
    public void newPuzzle2(int numToGuess) {
        int[][] base = {
            {3,8,1,7,9,2,5,4,6},
            {9,7,5,3,6,4,1,2,8},
            {6,2,4,8,1,5,7,3,9},
            {4,1,9,5,3,8,6,7,2},
            {8,3,6,1,2,7,9,5,4},
            {2,5,7,9,4,6,3,8,1},
            {1,4,2,6,7,3,8,9,5},
            {7,6,8,4,5,9,2,1,3},
            {5,9,3,2,8,1,4,6,7}
        };

        loadPuzzle(base, numToGuess);
    }

    /**
     * Copies a complete solution and randomly hides cells.
     * 
     * numToGuess = number of cells the user must fill (difficulty control)
     */
    private void loadPuzzle(int[][] source, int numToGuess) {
         if (numToGuess < 0 || numToGuess > GameBoard.GRID_SIZE * GameBoard.GRID_SIZE) {
            throw new IllegalArgumentException("numToGuess must be between 0 and 81.");
        }
        
        // Copy full solution and mark all cells as shown
        for (int row = 0; row < GameBoard.GRID_SIZE; row++) {
            for (int col = 0; col < GameBoard.GRID_SIZE; col++) {
                numbers[row][col] = source[row][col];
                isShown[row][col] = true;
            }
        }

        // Randomly hide cells to create the puzzle
        int cellsToHide = numToGuess;

        while (cellsToHide > 0) {
            int row = random.nextInt(GameBoard.GRID_SIZE);
            int col = random.nextInt(GameBoard.GRID_SIZE);

            // only hide cells that are still shown (avoid duplicates)
            if (isShown[row][col]) {
                isShown[row][col] = false;
                cellsToHide--;
            }
        }
    }
}