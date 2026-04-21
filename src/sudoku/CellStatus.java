package sudoku;

/**
 * Represents the current state of a Sudoku cell.
 */
public enum CellStatus {

    // Pre-filled cell from the puzzle (not editable)
    SHOWN,

    // Empty cell awaiting user input
    NO_GUESS,

    // Cell with correct user input
    CORRECT_GUESS,

    // Cell with incorrect user input
    WRONG_GUESS;

    // Returns true if the cell still needs user input.
    public boolean needsGuess() {
        return this == NO_GUESS || this == WRONG_GUESS;
    }

    // Returns true if the cell is already correctly solved.
    public boolean isSolved() {
        return this == SHOWN || this == CORRECT_GUESS;
    }
}