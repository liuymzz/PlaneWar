package entity.plane;

import entity.GameModel;
import enums.Dire;


public class Plane extends GameModel {
    private int hp;                     //飞机剩余的血量值
    private int maxHp;                  //飞机的最大血量值
    private Dire dire;                  //飞机类型


    public Dire getDire() {
        return dire;
    }

    public void setDire(Dire dire) {
        this.dire = dire;
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
