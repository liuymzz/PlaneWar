package entity.plane;

import entity.GameModel;

import java.awt.image.BufferedImage;

public class Plane extends GameModel {
    private int index = 0;              //当前显示图片索引
    private int maxIndex = 2;           //最大索引值
    private int changeInterval = 0;     //刷新的时间间隔
    private int hp;                     //飞机剩余的血量值
    private int maxHp;                  //飞机的最大血量值

    public BufferedImage getCurrImage() {
        BufferedImage currImage = null;
        changeInterval ++;

        currImage = getImage().getSubimage(index * getImage().getWidth() / maxIndex,0,getImage().getWidth() / maxIndex , getImage().getHeight());

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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
