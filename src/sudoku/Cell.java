package sudoku;

import java.awt.Color;
import java.awt.Font;

import javax.sound.sampled.Clip;
import javax.swing.JTextField;


/**
 * A customized JTextField that represents one Sudoku cell.
 */
public class Cell extends JTextField {
	private static final long serialVersionUID = 1L;

	// Colors used for shown cells, editable cells, and validation feedback
	public static final Color FG_SHOWN_DARK = Color.BLACK;
    public static final Color FG_SHOWN_LIGHT = Color.WHITE;
    public static final Color FG_NOT_SHOWN = Color.GRAY;

    public static final Color GAME1_ACCENT_BG = new Color(159, 243, 255); // pastel blue
    public static final Color GAME1_BASE_BG = new Color(255, 183, 227);   // pastel purple
    public static final Color GAME2_BASE_BG = new Color(70, 40, 140);     // dark purple
    public static final Color GAME2_ACCENT_BG = new Color(140, 30, 110);  // dark magenta

    public static final Color BG_NO_GUESS = Color.YELLOW;
    public static final Color BG_CORRECT_GUESS = new Color(44, 239, 89);
    public static final Color BG_WRONG_GUESS = new Color(255, 0, 0);

	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 24);

    // Sound effect played when the user enters a wrong answer
    private static final String WRONG_ANSWER_SOUND_FILE = "assets/sounds/wrongAnswer.wav"; 

	private static final AudioManager AUDIO_MANAGER = new AudioManager();
    private static final Clip WRONG_ANSWER_CLIP = AUDIO_MANAGER.loadClip(WRONG_ANSWER_SOUND_FILE);

	// Row and column number [0-8] of cell
    private final int row;
    private final int col;

    private int number; // puzzle number [1-9] for this cell
    private CellStatus status; // enumeration defined in CellStatus

    // Stores which board theme this cell belongs to, so it can repaint correctly later
    private int themeType;

	// Constructor
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        setHorizontalAlignment(JTextField.CENTER);
        setFont(FONT_NUMBERS);
    }

    // Initializes the cell for a new puzzle
	public void init(int number, boolean isShown) {
        this.number = number;
        this.status = isShown ? CellStatus.SHOWN : CellStatus.NO_GUESS;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColIndex() {
        return col;
    }

    public int getNumber() {
        return number;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public void setThemeType(int themeType) {
        this.themeType = themeType;
    }

    public int getThemeType() {
        return themeType;
    }

    // Repaints the cell using the theme assigned by GameBoard
    public void repaintByTheme() {
        switch (themeType) {
            case 1:
                paint1();
                break;
            case 2:
                paint2();
                break;
            case 3:
                paint3();
                break;
            case 4:
                paint4();
                break;
            default:
                paint2();
        }
    }

	// Game 1 accent subgrid style
    public void paint1() {
        applyTheme(GAME1_ACCENT_BG, FG_SHOWN_DARK);
    }

	// Game 1 base style
    public void paint2() {
        applyTheme(GAME1_BASE_BG, FG_SHOWN_DARK);
    }

	// Game 2 base style
    public void paint3() {
        applyTheme(GAME2_BASE_BG, FG_SHOWN_LIGHT);
    }

	// Game 2 accent subgrid style
    public void paint4() {
        applyTheme(GAME2_ACCENT_BG, FG_SHOWN_LIGHT);
    }

    // Applies the correct UI state based on whether the cell is shown, empty, correct, or wrong
    private void applyTheme(Color shownBackground, Color shownForeground) {
        if (status == CellStatus.SHOWN) {
            setText(String.valueOf(number));
            setEditable(false);
            setBackground(shownBackground);
            setForeground(shownForeground);
            return;
        }

        if (status == CellStatus.NO_GUESS) {
            setText("");
            setEditable(true);
            setBackground(BG_NO_GUESS);
            setForeground(FG_NOT_SHOWN);
            return;
        }

        if (status == CellStatus.CORRECT_GUESS) {
            setEditable(true);
            setBackground(BG_CORRECT_GUESS);
            return;
        }

        if (status == CellStatus.WRONG_GUESS) {
            setEditable(true);
            setBackground(BG_WRONG_GUESS);
			if (WRONG_ANSWER_CLIP != null && !WRONG_ANSWER_CLIP.isRunning()) {
                AUDIO_MANAGER.playOnce(WRONG_ANSWER_CLIP);
            }
        }
    }
}
