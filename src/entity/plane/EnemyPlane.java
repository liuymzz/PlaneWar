package entity.plane;

import enums.Dire;
import utils.Constants;

public class EnemyPlane extends Plane {
    private Dire dire;                      //敌机飞行的方向
    private boolean isDeathing;



    public boolean move() {
        switch (dire) {
            case DOWN:
                return moveDown();
            case LEFT:
                return moveLeft();
            case RIGHT:
                return moveRight();
        }


        return true;
    }

    private boolean moveRight() {
        setX(getX() + getSpeed());
        if (getX() >= Constants.WINDOW_WIDTH) {
            return false;
        }
        return true;
    }

    private boolean moveLeft() {
        setX(getX() - getSpeed());
        if (getX() <= -getImage().getWidth()) {
            return false;
        }
        return true;
    }

    private boolean moveDown() {
        setY(getY() + getSpeed());
        if (getY() >= Constants.WINDOW_HEIGHT) {
            return false;
        }
        return true;
    }

    public Dire getDire() {
        return dire;
    }

    public void setDire(Dire dire) {
        this.dire = dire;
    }

    public boolean isDeathing() {
        return isDeathing;
    }

    public void setDeathing(boolean deathing) {
        isDeathing = deathing;
    }
}
