package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * Main game window for the Sudoku application.
 */
public class SudokuFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	// Sound Clips & Icons
    private static final String HAPPY_MUSIC_FILE = "assets/sounds/happymusicBG.wav"; 
    private static final String FUN_MUSIC_FILE = "assets/sounds/funmusicBG.wav"; 
    private static final String SOUND_ICON_FILE = "assets/images/soundIcon.png";

	// Colors
	public static final Color PASTEL_PURPLE = new Color(255, 183, 227);
    public static final Color PASTEL_BLUE = new Color(159, 243, 255);
    public static final Color DARK_MAGENTA = new Color(140, 30, 110);
    public static final Color DARK_PURPLE  = new Color(70, 40, 140);

	private final GameBoard board;
    private final JButton game1Button;
    private final JButton game2Button;
    private final JLabel counterLabel;

	private final Font buttonFont = new Font(Font.SERIF, Font.BOLD, 18); 
    private final Font timerFont = new Font("Arial", Font.PLAIN, 15);

	// Java Internal Sound Clip
    private final AudioManager audioManager;
    private Clip happyMusicClip;
    private Clip funMusicClip;

	// Timer variables
	private Timer timer;
    private int second;
    private int minute;
    private final DecimalFormat timeFormat = new DecimalFormat("00");

    // Tracks whether background music is enabled from the menu
    private boolean isMusicEnabled = true;

    // Tracks which game mode is currently active, so the correct music can play
    private int currentGame = 1;

	// Constructor
	public SudokuFrame() {
        board = new GameBoard();
        game1Button = new JButton("Game 1");
        game2Button = new JButton("Game 2");
        counterLabel = new JLabel("00:00");
        audioManager = new AudioManager();

        initializeFrame();
        initializeBoard();
        initializeButtons();
        initializeMenuBar();
        initializeTimerLabel();
        loadAudio();

        // Stop game music and timer when the puzzle is completed
        board.setOnPuzzleSolved(() -> {
            stopAllMusic();

            if (timer != null) {
                timer.stop();
            }

            audioManager.playOnce(board.getCongratsClip());
            JOptionPane.showMessageDialog(this, "Congratulations!");
        });

        startNewGame1();
    }

    private void initializeFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(board, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
    }

	 private void initializeBoard() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 0, 0)); // set up the center button panel
        buttonPanel.add(game1Button);
        buttonPanel.add(game2Button);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
    }

    private void initializeButtons() {
        game1Button.setFont(buttonFont);
        game1Button.setBackground(PASTEL_PURPLE);
        game1Button.setForeground(Color.BLACK);
        game1Button.addActionListener(e -> startNewGame1());

        game2Button.setFont(buttonFont);
        game2Button.setBackground(DARK_MAGENTA);
        game2Button.setForeground(Color.WHITE);
        game2Button.addActionListener(e -> startNewGame2());
    }

	 private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu musicMenu = new JMenu("Music");

        java.net.URL soundIconUrl = getClass().getClassLoader().getResource(SOUND_ICON_FILE);
        ImageIcon soundIcon = soundIconUrl != null ? new ImageIcon(soundIconUrl) : null;

        JCheckBoxMenuItem musicToggleItem = new JCheckBoxMenuItem("Music On", true);
        if (soundIcon != null) {
            musicToggleItem.setIcon(soundIcon);
        }

        // Global music toggle: on/off for the current game only
        musicToggleItem.addActionListener(e -> {
            isMusicEnabled = musicToggleItem.isSelected();
            musicToggleItem.setText(isMusicEnabled ? "Music On" : "Music Off");

            if (isMusicEnabled) {
                playCurrentGameMusic();
            } else {
                stopAllMusic();
            }
        });

        musicMenu.add(musicToggleItem);
        menuBar.add(musicMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H); // alt short-cut key

        JMenuItem instructionsItem = new JMenuItem("Instructions", KeyEvent.VK_1); 
        instructionsItem.addActionListener(e -> showInstructions());
        helpMenu.add(instructionsItem);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
		
	 private void initializeTimerLabel() {
        counterLabel.setPreferredSize(new Dimension(getWidth(), 16));
        counterLabel.setLayout(new BoxLayout(counterLabel, BoxLayout.X_AXIS));
        counterLabel.setHorizontalAlignment(JTextField.CENTER);
        counterLabel.setFont(timerFont);

        getContentPane().add(counterLabel, BorderLayout.SOUTH);
    }

    private void loadAudio() {
        happyMusicClip = audioManager.loadClip(HAPPY_MUSIC_FILE);
        funMusicClip = audioManager.loadClip(FUN_MUSIC_FILE);
    }
	
	 private void startNewGame1() {
        currentGame = 1;
        board.init();
        resetAndStartTimer();
        stopAllMusic();

        if (isMusicEnabled) {
            audioManager.playLoop(happyMusicClip);
        }
    }

    private void startNewGame2() {
        currentGame = 2;
        board.init2();
        resetAndStartTimer();
        stopAllMusic();

        if (isMusicEnabled) {
            audioManager.playLoop(funMusicClip);
        }
    }

    // Plays only the background music for the active game mode
    private void playCurrentGameMusic() {
        stopAllMusic();

        if (currentGame == 1) {
            audioManager.playLoop(happyMusicClip);
        } else if (currentGame == 2) {
            audioManager.playLoop(funMusicClip);
        }
    }

    private void stopAllMusic() {
        audioManager.stopClip(happyMusicClip);
        audioManager.stopClip(funMusicClip);
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(
            this,
            "Enter a number from 1 to 9 in the yellow boxes.\n"
                + "Press Enter on your keyboard to check your answer for each box.\n"
                + "The number cannot be repeated in the same row, column, and subgrid of 9 boxes.\n"
                + "To restart your game, simply press the game number again.",
            "How to play?",
            JOptionPane.QUESTION_MESSAGE
        );
    }

    // Resets the timer whenever a new game starts
    private void resetAndStartTimer() {
        if (timer != null) {
            timer.stop();
        }

        second = 0;
        minute = 0;
        updateTimerLabel();

        timer = new Timer(1000, e -> {
            second++;

            if (second == 60) {
                second = 0;
                minute++;
            }

            updateTimerLabel();
        });

        timer.start();
    }

    private void updateTimerLabel() {
        counterLabel.setText(timeFormat.format(minute) + ":" + timeFormat.format(second));
    }
}
