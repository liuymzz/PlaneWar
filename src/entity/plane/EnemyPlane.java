package entity.plane;

import utils.Constants;

public class EnemyPlane extends Plane {
    private boolean isDeathing;



    public boolean move() {
        switch (getDire()) {
            case DOWN:
                return moveDown();
            case LEFT:
                return moveLeft();
            case RIGHT:
                return moveRight();
            case UP:
                return moveUP();
        }


        return true;
    }

    private boolean moveUP() {
        setY(getY() - getSpeed());
        if (getY() <= 0){
            return false;
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

    public boolean isDeathing() {
        return isDeathing;
    }

    public void setDeathing(boolean deathing) {
        isDeathing = deathing;
    }
}
