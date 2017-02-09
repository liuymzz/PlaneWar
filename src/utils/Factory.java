package utils;

import entity.bullets.Bullet;
import entity.plane.Plane;

import java.util.List;

public class Factory {

    /**
     *
     * @param plane 为哪架飞机生成子弹
     * @param list 存放子弹的集合
     */
    public static void generateBullet(Plane plane , List<Bullet> list){
        Bullet bullet = new Bullet();
        bullet.setX(plane.getX() + (plane.getWidth() / 2) - bullet.getWidth() / 2);     //子弹的x坐标等于飞机的x坐标加上他宽度的一半再减去子弹宽度的一半
        bullet.setY(plane.getY() - 2);
        list.add(bullet);
    }

}
