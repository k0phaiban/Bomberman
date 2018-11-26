package gameSound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameSound {
        public static GameSound instance;
        private HashMap<String,Clip> audioMap;
        public static final String PLAYGAME = "playgame.mid";
	public static final String BOMB = "newbomb.wav";
	public static final String BOMBER_DIE = "bomber_die.wav";
	public static final String ENEMY_DIE = "Enemy_die.wav";
	public static final String BOMB_NO = "bomb_bang.wav";
	public static final String ITEM = "item.wav";
	public static final String WIN = "win.wav";
	public static final String LOSE = "lose.mid";
        public GameSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		audioMap = new HashMap<>();
		loadAllAudio();
	}
        public static GameSound getIstance() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		if (instance == null) {
			instance = new GameSound();
		}

		return instance;
        }
	public void loadAllAudio() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		putAudio(PLAYGAME);
		putAudio(BOMB);
		putAudio(ENEMY_DIE);
		putAudio(BOMBER_DIE);
		putAudio(BOMB_NO);
		putAudio(ITEM);
		putAudio(WIN);
		putAudio(LOSE);
	}
        public void putAudio(String name) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            File sound=new File(name);
            Clip clip=AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            audioMap.put(name, clip);
	}
        public Clip getAudio(String name) {
		return audioMap.get(name);
	}
        public void PlaySound(String name) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException{
            File sound=new File(name);
            Clip clip=AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();         
        }
}
