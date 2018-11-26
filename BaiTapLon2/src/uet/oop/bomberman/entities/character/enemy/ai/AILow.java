package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;

public class AILow extends AI {
        
	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đư�?ng đi
		return random.nextInt(4);
	}
}
