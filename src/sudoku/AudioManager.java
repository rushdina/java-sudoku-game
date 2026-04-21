package sudoku;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * AudioManager handles all audio-related operations for the Sudoku application.
 * 
 * Used by:
 * - SudokuFrame → background music (Game 1 / Game 2)
 * - GameBoard / Cell → sound effects (wrong answer, congrats)
 */
public class AudioManager {

    public Clip loadClip(String fileName) {
        try {
            // Load file from resources (assets folder inside classpath)
            URL url = getClass().getClassLoader().getResource(fileName);

            // If file is missing → prevent crash and log error
            if (url == null) {
                System.err.println("Could not find audio file: " + fileName);
                return null;
            }

            // Convert file into audio stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            // Create Clip (memory-based audio player)
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream); // load audio data into memory
            return clip;
        } catch (Exception e) {
            System.err.println("Failed to load audio file: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    // Plays background music in a continuous loop
    public void playLoop(Clip clip) {
        if (clip == null) {
            return;
        }

        // Stop current playback if already running (avoid overlapping)
        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0); // rewind to start
        clip.start();  // start playback
        clip.loop(Clip.LOOP_CONTINUOUSLY); // repeat forever
    }

    // Stops any currently playing audio clip
    public void stopClip(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Plays a sound effect once (no looping) 
    public void playOnce(Clip clip) {
        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0); 
        clip.start(); 
    }
}