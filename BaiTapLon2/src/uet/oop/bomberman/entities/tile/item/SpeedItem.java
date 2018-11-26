package uet.oop.bomberman.entities.tile.item;

import gameSound.GameSound;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class SpeedItem extends Item {

	public SpeedItem(int x, int y, int level, Sprite sprite) {
		super(x, y, level, sprite);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
		 if (e instanceof Bomber){
                    ((Bomber)e).addPowerup(this);
                    remove(); 
                    return true; 
                }
		return false;
	}
        @Override
        public void setValues() {
            _active = true; 
            try {
                GameSound.getIstance().PlaySound(GameSound.ITEM);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(SpeedItem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(SpeedItem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SpeedItem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(SpeedItem.class.getName()).log(Level.SEVERE, null, ex);
            }
            Game.addBomberSpeed(2);
        }
}
