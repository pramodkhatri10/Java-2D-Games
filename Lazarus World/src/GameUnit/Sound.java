package GameUnit;

import GameWorld.GameWorld;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    public GameWorld gameWorld;
    private Clip clip;


    //validates sound file
    public Sound(GameWorld lazarus, String filename) {
        this.gameWorld = lazarus;
        try {
            File file = new File(filename);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } else {
                throw new RuntimeException("ERROR: sound file not found - " + filename); //throws run time exception as it could not find the file
            }
        } catch (UnsupportedAudioFileException exception) {
            exception.printStackTrace();
            throw new RuntimeException("ERROR: cannot support audio - " + exception); //unsupported audio

        } catch (LineUnavailableException exception) {
            exception.printStackTrace();
            throw new RuntimeException("ERROR: Line Unavailable Error - " + exception); //line unavailable
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("ERROR: IO - " + exception); //input output exception

        }

    }

    public void soundPlay() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void iterate() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void abrupt() {

        clip.stop();
    }
}
