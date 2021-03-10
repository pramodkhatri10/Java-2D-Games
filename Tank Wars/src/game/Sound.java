package game;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private AudioInputStream soundStream;
    private String soundFile;
    private Clip clip;
    private int type;



    public Sound(int type, String soundFile){
        this.soundFile = soundFile;
        this.type = type;
        try{
            soundStream = AudioSystem.getAudioInputStream(Sound.class.getResource(soundFile));
            clip = AudioSystem.getClip();
            clip.open(soundStream);
        }
        catch(Exception e){
            System.out.println(e.getMessage() + " JAR file cannot read sound files.");
        }
        if(this.type == 1){
            Runnable myRunnable = new Runnable(){
                public void run(){
                    while(true){
                        clip.start();
                        clip.loop(clip.LOOP_CONTINUOUSLY);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }
}