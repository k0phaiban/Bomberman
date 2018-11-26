package uet.oop.bomberman.entities.character;

import gameSound.GameSound;
import java.io.IOException;
import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;


public class Bomber extends Character {
    
    private List<Bomb> _bombs;
    protected Keyboard _input;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset v�? 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;
    public static List<Item> _listItem = new ArrayList<Item>();
    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím đi�?u khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có th�?a mãn hay không
        // TODO:  Game.getBombRate() sẽ trả v�? số lượng bom có thể đặt liên tiếp tại th�?i điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng th�?i gian quá ngắn
        // TODO: nếu 3 đi�?u kiện trên th�?a mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs v�? 0
        if(_input.space && Game.getBombRate()>0 && _timeBetweenPutBombs<0){
            int x= Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int y= Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() );			
            placeBomb(x,y);
            Game.addBombRate(-1);		
            _timeBetweenPutBombs = 0;
	}
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb bom=new Bomb(x,y,_board);
        _board.addBomb(bom);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
        try {
            GameSound.getIstance().getAudio(GameSound.BOMBER_DIE).start();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Bomber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Bomber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bomber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu đi�?u khiển hướng đi từ _input và g�?i move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị c�? _moving khi thay đổi trạng thái di chuyển
        int x = 0, y = 0;
        if(_input.up) y--;
        if(_input.down) y++;
        if(_input.left) x--;
        if(_input.right) x++;
        if(x != 0 || y != 0)  {
                move(x*Game.getBomberSpeed(),y*Game.getBomberSpeed());
                _moving = true;
        } else {
                _moving = false;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
        for (int c = 0; c < 4; c++) {
            double xt = ((_x + x) + c % 2 * 11) / Game.TILES_SIZE;
            double yt = ((_y + y) + c / 2 * 12 - 13) / Game.TILES_SIZE;	
            Entity a = _board.getEntity(xt, yt, this);
            if(!a.collide(this)) return false;
	}	
	return true;
    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi t�?a độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        if(xa > 0) _direction = 1;
        else if(xa < 0) _direction = 3;
	if(ya > 0) _direction = 2;
        else if(ya < 0) _direction = 0;		
	if(canMove(0, ya)) _y += ya;
	if(canMove(xa, 0)) _x += xa;
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if(e instanceof Flame) {
            kill();
            return false;
	}	
	if(e instanceof Enemy) {
            kill();
            return true;
	}
        return true;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
    public void addPowerup(Item p) {
        if(p.isRemoved()) return;
        _listItem.add(p);
        p.setValues();       
    }

    public void clearUsedPowerups() {
        Item p;
        for (int i = 0; i < _listItem.size(); i++) {
                p = _listItem.get(i);
                if(p.isActive() == false)
                        _listItem.remove(i);
        }
    }

    public void removePowerups() {
        for (int i = 0; i < _listItem.size(); i++) {
                        _listItem.remove(i);
        }
        
    }
}
