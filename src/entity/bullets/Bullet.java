package entity.bullets;

import entity.GameModel;
import utils.Constants;
import utils.Medias;


public class Bullet extends GameModel {
    private int attack;

    public Bullet() {
        setImage(Medias.getImage("m1.png"));
        setSpeed(10);
        setAttack(20);
        setWidth(getImage().getWidth());
        setHeight(getImage().getHeight());
    }



    public boolean moveUp() {
        setY(getY() - getSpeed());
        if (getY() <= 0) {
            return false;
        }
        return true;
    }

    public boolean moveDown() {
        setY(getY() + getSpeed());
        if (getY() >= Constants.WINDOW_HEIGHT) {
            return false;
        }
        return true;
    }

    public boolean moveRight() {
        setX(getX() + getSpeed());
        if (getX() >= Constants.WINDOW_WIDTH) {
            return false;
        }
        return true;
    }

    public boolean moveLeft() {
        setX(getX() - getSpeed());
        if (getX() <= 0) {
            return false;
        }
        return true;
    }



    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
}
