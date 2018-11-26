package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	
	public AIMedium(Bomber bomber, Enemy e) {
		_bomber = bomber;
		_e = e;
	}

	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đư�?ng đi
		int director = random.nextInt(4);
                if (_bomber == null || director >= 3) {
                    return random.nextInt(4);
                }
                if (director == 1) {
                    int v = calculateRowDirection();
                    if (v != -1) {
                        return v;
                    } else {
                        return calculateColDirection();
                    }

                }
                if (director == 2) {
                    int h = calculateColDirection();

                    if (h != -1) {
                        return h;
                    } else {
                        return calculateRowDirection();
                    }
                }
                return 4;
	}
        // add function. 
        
        protected int calculateColDirection() {
		if(_bomber.getXTile() < _e.getXTile())
			return 3;
		else if(_bomber.getXTile() > _e.getXTile())
			return 1;
		
		return -1;
	}
	
	protected int calculateRowDirection() {
		if(_bomber.getYTile() < _e.getYTile())
			return 0;
		else if(_bomber.getYTile() > _e.getYTile())
			return 2;
		return -1;
	}
}
