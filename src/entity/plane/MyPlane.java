package entity.plane;


import entity.bullets.Bullet;
import utils.Constants;
import utils.Medias;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyPlane extends Plane {
    private List<Bullet> myBullets = new ArrayList();
    private int changeInterval = 0;     //刷新的时间间隔


    public MyPlane(){
        setImage(Medias.getImage("fighter.png"));
        setSpeed(8);
        setIndex(0);
        setMaxIndex(2);
        setHeight(getImage().getHeight());
        setWidth(getWidth() / 2);
        setY(Constants.WINDOW_HEIGHT - getImage().getHeight());
        setX((Constants.WINDOW_WIDTH - getImage().getWidth()) / getMaxIndex());
        setHp(1000);
        setMaxHp(1000);

    }


    public BufferedImage getCurrImage() {
        BufferedImage currImage;
        changeInterval ++;

        currImage =
                getImage().getSubimage(
                        getIndex() * getImage().getWidth() / getMaxIndex(),
                        0,
                        getImage().getWidth() / getMaxIndex() ,
                        getImage().getHeight()
                );

        if(changeInterval >= 5){
            changeInterval = 0;
            if (getIndex() >= (getMaxIndex() - 1)) {
                setIndex(0);
            } else {
                setIndex(getIndex() + 1);
            }
        }

        return currImage;
    }

    @Override
    public Rectangle getHurtArea() {
        return super.getHurtArea();
    }

    public void moveUp() {
        setY(getY() - getSpeed());
        if (getY() <= 0) {
            setY(0);
        }
    }

    public void moveDown() {
        setY(getY() + getSpeed());
        if (getY() >= (Constants.WINDOW_HEIGHT - getImage().getHeight())) {
            setY(Constants.WINDOW_HEIGHT - getImage().getHeight());
        }
    }

    public void moveLeft() {
        setX(getX() - getSpeed());
        if (getX() <= 0) {
            setX(0);
        }
    }

    public void moveRight() {
        setX(getX() + getSpeed());
        if (getX() >= (Constants.WINDOW_WIDTH - getImage().getWidth() / getMaxIndex())) {
            setX(Constants.WINDOW_WIDTH - getImage().getWidth() / getMaxIndex());
        }
    }

    public List<Bullet> getMyBullets() {
        return myBullets;
    }

    public void setMyBullets(List<Bullet> myBullets) {
        this.myBullets = myBullets;
    }

}
