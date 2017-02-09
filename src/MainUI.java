import entity.GameMap;
import entity.GameModel;
import entity.plane.MyPlane;
import entity.plane.Plane;
import utils.Constants;
import utils.Factory;
import utils.Medias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame implements Runnable{
    private HB hb = new HB();
    private GameMap map = new GameMap();
    private MyPlane myPlane = new MyPlane();
    private int generateBulletInterval = 0;                                 //产生子弹的间隔
    private boolean UP, DOWN, LEFT, RIGHT;                                  //判断方向键是否按下
    private List<Plane> enemyPlanes = new ArrayList<>();                    //存放敌机的集合
    private int enemyPlanesGenerateInterval = 0;                            //敌机产生的间隔时间

    public MainUI(){
        setSize(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);


        Container c = getContentPane();
        c.setFocusable(true);
        c.add(hb);
        c.addKeyListener(new KeyAd());

        setVisible(true);
    }

    class KeyAd extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    UP = true;
                    break;
                case KeyEvent.VK_DOWN:
                    DOWN = true;
                    break;
                case KeyEvent.VK_LEFT:
                    LEFT = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    RIGHT = true;
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    UP = false;
                    break;
                case KeyEvent.VK_DOWN:
                    DOWN = false;
                    break;
                case KeyEvent.VK_LEFT:
                    LEFT = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    RIGHT = false;
                    break;
            }
        }
    }

    class HB extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            //g.clearRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            drawMap(g);

            //玩家飞机的影子
            g.drawImage(Medias.getImage("fighter_shadow.png"),myPlane.getX() + 10 , myPlane.getY() + 10,this);

            //画玩家飞机
            g.drawImage(myPlane.getCurrImage(),myPlane.getX(),myPlane.getY(),this);

            drawMyPlaneBullets(g);
        }

        private void drawMyPlaneBullets(Graphics g) {
            for (int i = 0 ; i < myPlane.getMyBullets().size() ; i++){
                drawGameModel(g,myPlane.getMyBullets().get(i));
            }
        }

        private void drawGameModel(Graphics g, GameModel model) {
            g.drawImage(model.getImage(),model.getX(),model.getY(),this);
        }

        private void drawMap(Graphics g) {
            BufferedImage top = map.getTopImage();
            BufferedImage buttom = map.getButtomImage();
            g.drawImage(top, 0, 0, this);
            g.drawImage(buttom, 0, top.getHeight(), this);
        }
    }




    @Override
    public void run() {
        while (1 == 1){

            map.move();
            generateBullet();
            moveBullet();
            moveMyPlane();
            hb.repaint();


            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBullet() {

        for (int i = 0 ; i < myPlane.getMyBullets().size() ; i ++){
            if(myPlane.getMyBullets().get(i).moveUp() == false){
                myPlane.getMyBullets().remove(i);
                i --;
            }
        }
    }

    private void generateBullet() {
        generateBulletInterval ++;
        if(generateBulletInterval >= 10){
            Factory.generateBullet(myPlane,myPlane.getMyBullets());
            generateBulletInterval = 0;
        }
    }

    private void moveMyPlane() {
        if (UP) {
            myPlane.moveUp();
        }
        if (DOWN) {
            myPlane.moveDown();
        }
        if (RIGHT) {
            myPlane.moveRight();
        }
        if (LEFT) {
            myPlane.moveLeft();
        }
    }

    public static void main(String[] args){
        MainUI ui = new MainUI();
        new Thread(ui).start();
    }
}
