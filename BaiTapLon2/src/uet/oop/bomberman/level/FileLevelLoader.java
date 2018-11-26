package uet.oop.bomberman.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.entities.character.enemy.Minvo;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đ�?c được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;
	
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}
	
	@Override
	public void loadLevel(int level) {
		// TODO: đ�?c dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đ�?c được vào _width, _height, _level, _map
        BufferedReader buf = null;
            try {
                buf = new BufferedReader(new  FileReader(new File("C:\\Users\\Admin\\Documents\\NetBeansProjects\\BaiTapLon2\\res\\levels\\Level"+level+".txt")));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileLevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        String integer = null;
            try {
                integer = buf.readLine();
            } catch (IOException ex) {
                Logger.getLogger(FileLevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        String[] itg=integer.trim().split(" ");
        _level=Integer.parseInt(itg[0]);
        _height=Integer.parseInt(itg[1]);
        _width=Integer.parseInt(itg[2]);
        _map=new char[_height][_width];
        for(int i=0;i<_height;i++){
            String s = null;
            try {
                s = buf.readLine();
            } catch (IOException ex) {
                Logger.getLogger(FileLevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(int j=0;j<_width;j++){
                    _map[i][j]=s.charAt(j);
                }
            }
            try {
                buf.close();
            } catch (IOException ex) {
                Logger.getLogger(FileLevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, g�?i _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
		// thêm Wall
                for(int y=0;y<_height;y++){
                    for(int x=0;x<_width;x++){
                        int pos=x+y*_width;
                        switch(_map[y][x]){
                            case '#':
                                _board.addEntity(pos,new Wall(x,y,Sprite.wall));
                                break;
                            case '*':
                                _board.addEntity(pos,new LayeredEntity(x, y,new Grass(x ,y, Sprite.grass),new Brick(x ,y, Sprite.brick)) );
                                break;
                            case ' ': 
				_board.addEntity(pos, new Grass(x, y, Sprite.grass) );
				break;
                            case 'x':
                                _board.addEntity(pos, new LayeredEntity(x, y, new Grass(x ,y, Sprite.grass),new Portal(x ,y,_board, Sprite.portal),new Brick(x ,y, Sprite.brick)));                              
                                break;
                            case 'p':
                                _board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
                                Screen.setOffset(0, 0);
                                _board.addEntity(pos, new Grass(x,y, Sprite.grass));
                                break;
                            case '1':
                                _board.addCharacter( new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                                _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                                break;
                            case '2':
                                _board.addCharacter( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                                _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                                break;
                            case '3':
                                _board.addCharacter( new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                                _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                                break;
                            case '4':
                                _board.addCharacter( new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                                _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                                break;
                            case '5':
                                _board.addCharacter( new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                                _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                                break;
                            case 'b':
                                LayeredEntity layer = new LayeredEntity(x, y, 
						new Grass(x ,y, Sprite.grass), 
						new Brick(x ,y, Sprite.brick));
				
				if(_board.isItemUsed(x, y, _level) == false) {
					layer.addBeforeTop(new BombItem(x, y, _level, Sprite.powerup_bombs));
				}
				
				_board.addEntity(pos, layer);
				break;
                            case 'f':
                                layer = new LayeredEntity(x, y, 
                                                            new Grass(x ,y, Sprite.grass), 
                                                            new Brick(x ,y, Sprite.brick));

                                            if(_board.isItemUsed(x, y, _level) == false) {
                                                    layer.addBeforeTop(new FlameItem(x, y, _level, Sprite.powerup_flames));
                                            }

                                            _board.addEntity(pos, layer);
                                            break;
                            case 's':
                                layer = new LayeredEntity(x, y, 
                                                            new Grass(x ,y, Sprite.grass), 
                                                            new Brick(x ,y, Sprite.brick));

                                            if(_board.isItemUsed(x, y, _level) == false) {
                                                    layer.addBeforeTop(new SpeedItem(x, y, _level, Sprite.powerup_speed));
                                            }

                                            _board.addEntity(pos, layer);
                                            break;
                            default:
                               _board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                               break;
                        }
                    }
                }
	}

}
