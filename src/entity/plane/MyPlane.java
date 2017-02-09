package entity.plane;


import entity.bullets.Bullet;
import utils.Constants;
import utils.Medias;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyPlane extends Plane {
    private List<Bullet> myBullets = new ArrayList();
    private int changeInterval = 0;     //刷新的时间间隔
    private int index = 0;              //当前显示图片索引
    private int maxIndex = 2;           //最大索引值


    public MyPlane(){
        setImage(Medias.getImage("fighter.png"));
        setSpeed(8);
        setIndex(0);
        setMaxIndex(2);
        setHeight(getImage().getHeight());
        setWidth(getImage().getWidth() / 2);
        setY(Constants.WINDOW_HEIGHT - getImage().getHeight());
        setX((Constants.WINDOW_WIDTH - getImage().getWidth()) / getMaxIndex());
    }


    public BufferedImage getCurrImage() {
        BufferedImage currImage;
        changeInterval ++;

        currImage =
                getImage().getSubimage(
                        index * getImage().getWidth() / maxIndex,
                        0,
                        getImage().getWidth() / maxIndex ,
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }
}
