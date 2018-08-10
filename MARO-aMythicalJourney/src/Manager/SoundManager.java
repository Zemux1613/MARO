package Manager;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundManager {

    private static SoundManager instance;
    private static HashMap<Sound, Clip> list;

    public static SoundManager getInstance() {

        if (instance == null) {
            instance = new SoundManager();
        }

        return instance;

    }

    public SoundManager() {
        list = new HashMap<>();
    }

    private void loadSound(Sound sound) {
        if (!list.containsKey(sound)) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound.getPath()));
                AudioFormat af = audioInputStream.getFormat();
                int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
                byte[] audio = new byte[size];
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);
                audioInputStream.read(audio, 0, size);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(af, audio, 0, size);
                list.put(sound, clip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playSound(Sound sound) {
        try {
            if (!list.containsKey(sound)) {
                loadSound(sound);
            }
            Clip clip = list.get(sound);
            clip.setMicrosecondPosition(0);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public enum Sound {

        TEST("src/Sounds/glassbell.wav"),
        TEST2("src/Sounds/chipquest.wav");

        String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
