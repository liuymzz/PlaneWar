package entity;

import utils.Medias;

import java.awt.image.BufferedImage;

public class BOOM {
    private int x;
    private int y;
    private int index = 0;
    private int maxIndex = 7;
    private BufferedImage image = Medias.getImage("boom.png");
    private int changeInterval = 0;

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

        if(changeInterval >= 10){
            changeInterval = 0;
                setIndex(getIndex() + 1);
        }

        return currImage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
