import entity.BOOM;
import entity.GameMap;
import entity.GameModel;
import entity.Meteor;
import entity.bullets.Bullet;
import entity.plane.EnemyPlane;
import entity.plane.MyPlane;
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

public class MainUI extends JFrame implements Runnable {
    private HB hb = new HB();
    private GameMap map = new GameMap();
    private MyPlane myPlane = new MyPlane();
    private int generateBulletInterval = 0;                                 //产生子弹的间隔
    private boolean UP, DOWN, LEFT, RIGHT;                                  //判断方向键是否按下
    private List<EnemyPlane> enemyPlanes = new ArrayList<>();               //存放敌机的集合
    private int enemyPlanesGenerateInterval = 0;                            //敌机产生的间隔时间
    private List<BOOM> booms = new ArrayList<>();                           //存放所有的爆炸效果
    private List<Meteor> meteors = new ArrayList<>();                       //存放陨石的集合
    private int meteotGenerateInteval = 0;                                  //陨石产生的时间间隔

    public MainUI() {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);


        Container c = getContentPane();
        c.setFocusable(true);
        c.add(hb);
        c.addKeyListener(new KeyAd());

        setVisible(true);
    }

    class KeyAd extends KeyAdapter {
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

    class HB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            //g.clearRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            drawMap(g);

            //玩家飞机的影子
            g.drawImage(Medias.getImage("fighter_shadow.png"), myPlane.getX() + 10, myPlane.getY() + 10, this);

            //画玩家飞机
            g.drawImage(myPlane.getCurrImage(), myPlane.getX(), myPlane.getY(), this);

            //画玩家飞机的子弹
            drawMyPlaneBullets(g);

            //画敌机
            drawEnemyPlanes(g);

            //画爆炸效果
            drawBoom(g);

            //画陨石
            drawMeteor(g);
        }

        private void drawMeteor(Graphics g) {
            for (int i = 0; i < meteors.size(); i++) {
                Meteor meteor = meteors.get(i);
                drawGameModel(g, meteor);
            }
        }

        private void drawBoom(Graphics g) {
            for (int i = 0; i < booms.size(); i++) {
                BOOM boom = booms.get(i);
                g.drawImage(boom.getCurrImage(), boom.getX(), boom.getY(), this);
                if (boom.getIndex() >= boom.getMaxIndex()) {
                    booms.remove(i);
                    i--;
                }
            }
        }

        private void drawEnemyPlanes(Graphics g) {
            for (int i = 0; i < enemyPlanes.size(); i++) {
                drawGameModel(g, enemyPlanes.get(i));
            }
        }

        private void drawMyPlaneBullets(Graphics g) {
            for (int i = 0; i < myPlane.getMyBullets().size(); i++) {
                drawGameModel(g, myPlane.getMyBullets().get(i));
            }
        }

        private void drawGameModel(Graphics g, GameModel model) {
            g.drawImage(model.getImage(), model.getX(), model.getY(), this);
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
        while (1 == 1) {

            map.move();
            generateBullet();
            moveBullet();
            moveMyPlane();
            generateEnemyPlane();
            moveEnemyPlane();
            generateMeteor();
            moveMeteor();
            hb.repaint();


            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveMeteor() {
        for (int i = 0; i < meteors.size(); i++) {
            Meteor meteor = meteors.get(i);
            if (meteor.move() == false) {
                meteors.remove(i);
                i--;
            }
        }
    }

    private void generateMeteor() {
        meteotGenerateInteval++;
        if (meteotGenerateInteval > 40) {
            Factory.generateMeteor(meteors);
            meteotGenerateInteval = 0;
        }
    }

    private void moveEnemyPlane() {
        for (int i = 0; i < enemyPlanes.size(); i++) {
            if (enemyPlanes.get(i).move() == false) {
                enemyPlanes.remove(i);
                i--;
            }
        }
    }

    private void generateEnemyPlane() {
        enemyPlanesGenerateInterval++;
        if (enemyPlanesGenerateInterval > 20) {
            Factory.generateEnemyPlane(enemyPlanes);
            enemyPlanesGenerateInterval = 0;
        }
    }

    private void moveBullet() {
        List<Bullet> bullets = myPlane.getMyBullets();
        boolean isRemove = false;                           //用于判断当前比较的子弹是否被消除，防止产生空异常

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.moveUp() == false) {
                bullets.remove(i);
                i--;
                continue;
            }

            //遍历是否击中敌机
            for (int j = 0; j < enemyPlanes.size(); j++) {
                EnemyPlane enemyPlane = enemyPlanes.get(j);
                if (bullet.getHurtArea().intersects(enemyPlane.getHurtArea())) {
                    enemyPlane.setHp(enemyPlane.getHp() - bullet.getAttack());
                    if (enemyPlane.getHp() < 0) {
                        enemyPlanes.remove(j);
                        addBoom(enemyPlane);

                    }
                    isRemove = true;
                    bullets.remove(i);
                    i--;
                    break;
                }
            }

            //遍历是否击中陨石
            if (isRemove == false) {
                for (int j = 0; j < meteors.size(); j++) {
                    Meteor meteor = meteors.get(j);
                    if (bullet.getHurtArea().intersects(meteor.getHurtArea())) {
                        meteor.setHp(meteor.getHp() - bullet.getAttack());
                        if (meteor.getHp() < 0) {
                            meteors.remove(j);
                            addBoom(meteor);

                        }
                        bullets.remove(i);
                        i--;
                        break;
                    }
                }
            }

        }
    }

    private void addBoom(GameModel gameModel){
        BOOM boom = new BOOM();
        boom.setX(gameModel.getX());
        boom.setY(gameModel.getY());
        booms.add(boom);
    }

    private void generateBullet() {
        generateBulletInterval++;
        if (generateBulletInterval >= 10) {
            Factory.generateBullet(myPlane, myPlane.getMyBullets());
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

    public static void main(String[] args) {
        MainUI ui = new MainUI();
        new Thread(ui).start();
    }
}
