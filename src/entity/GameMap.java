package entity;


import utils.Constants;
import utils.Medias;

import java.awt.image.BufferedImage;

public class GameMap {
    private int index = 1;
    private int gapY = 1;           //两张图片之间的y的值
    private int mapNum = 1;


    public BufferedImage getTopImage(){
        int tmpIndex = index + 1;
        if(index >= 4){
            tmpIndex = 1;
        }
        return Medias
                .getImage("map"+mapNum+"_"+tmpIndex+".jpg")
                .getSubimage(
                        0,
                        Constants.WINDOW_HEIGHT - gapY,
                        Constants.WINDOW_WIDTH,
                        gapY
                );
    }

    public BufferedImage getButtomImage(){
        return Medias
                .getImage("map"+mapNum+"_"+index+".jpg")
                .getSubimage(
                        0,
                        0,
                        Constants.WINDOW_WIDTH,
                        Constants.WINDOW_HEIGHT - gapY
                );

    }

    public void move(){
        gapY += 2;
        if(gapY >= Constants.WINDOW_HEIGHT){
            gapY = 1;
            index++;
            if(index > 4){
                index = 1;
            }
        }
    }

    public void changeMapNum(){
        mapNum ++;
        if(mapNum > 3){
            mapNum = 1;
        }
    }
}
