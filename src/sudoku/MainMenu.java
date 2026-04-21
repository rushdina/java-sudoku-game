package sudoku;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main Menu screen for Sudoku game
 */
public class MainMenu extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainMenu() {
        initializeUI();
    }

	private void initializeUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		// Top panel: displays game logo (fallback to text if image not found)
        JPanel topPanel = new JPanel();
        URL logoUrl = getClass().getClassLoader().getResource("assets/images/logo.png");
        if (logoUrl != null) {
            topPanel.add(new JLabel(new ImageIcon(logoUrl)));
        } else {
            topPanel.add(new JLabel("Sudoku"));
        }

		// Bottom panel: contains main menu actions
        JPanel bottomPanel = new JPanel();

        JButton playButton = new JButton("PLAY");
        playButton.addActionListener(e -> {
            // Launch main Sudoku game window
            SudokuFrame frame = new SudokuFrame();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            dispose(); // close menu after starting game
        });

		JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> {
            // Confirm before exiting application
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        bottomPanel.add(playButton);
        bottomPanel.add(exitButton);

        cp.add(topPanel, BorderLayout.CENTER);
        cp.add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Sudoku Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // handle window closing
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // center window screen
        setVisible(true);
    }
}
