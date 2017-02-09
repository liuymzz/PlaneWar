package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameModel {
    private int x;                      //x坐标
    private int y;                      //y坐标
    private int speed;                  //移动速度
    private BufferedImage image;        //图像
    public Rectangle hurtArea;          //伤害区域
    private int height;                 //高
    private int width;                  //宽

    public Rectangle getHurtArea() {
        hurtArea.setBounds(
                getX() + 10,
                getY() + 10,
                getImage().getWidth() - 10,
                getImage().getHeight() - 10
        );
        return hurtArea;
    }

    public void setHurtArea(Rectangle hurtArea) {
        this.hurtArea = hurtArea;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


}
