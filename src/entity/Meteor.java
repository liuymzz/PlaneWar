package entity;

import utils.Constants;
import utils.Medias;

public class Meteor extends GameModel {
    private int hp = 200;

    public Meteor(){
        setImage(Medias.getImage("meteor.png"));
        setSpeed(2);
    }

    public Meteor(int x,int y){

        setX(x);
        setY(y);
    }

    public boolean move(){
        setY(getY() + getSpeed());
        if(getY() > Constants.WINDOW_HEIGHT){
            return false;
        }

        return true;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
