import entity.BOOM;
import entity.GameMap;
import entity.GameModel;
import entity.Meteor;
import entity.bullets.Bullet;
import entity.plane.EnemyPlane;
import entity.plane.MyPlane;
import enums.BulletType;
import enums.GameState;
import utils.Constants;
import utils.Factory;
import utils.Medias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int meteortGenerateInterval = 0;                                  //陨石产生的时间间隔
    private List<Bullet> enemyBullets = new ArrayList<>();                  //存放敌机子弹的集合
    private int enemyBulletGenerateInterval = 0;                            //敌机子弹产生时间间隔

    private GameState state = GameState.WELCOME;                            //游戏全局状态

    private GameModel startGame = new GameModel();                          //开始游戏按钮
    private GameModel exit = new GameModel();                               //退出按钮
    private GameModel resume = new GameModel();                             //重新开始按钮
    private GameModel defeat = new GameModel();                             //游戏失败


    public MainUI() {
        ////游戏元素初始化

        //开始游戏按钮参数
        startGame.setImage(Medias.getImage("btn_play.png"));
        startGame.setX(Constants.WINDOW_WIDTH / 2 - startGame.getWidth() / 2);
        startGame.setY(Constants.WINDOW_HEIGHT + startGame.getHeight());
        //退出按钮参数
        exit.setImage(Medias.getImage("exit.png"));
        exit.setX(350);
        exit.setY(580);
        //重新开始按钮
        resume.setImage(Medias.getImage("restart1.png"));
        resume.setX(350);
        resume.setY(650);
        //失败
        defeat.setImage(Medias.getImage("defeat.png"));
        defeat.setX(Constants.WINDOW_WIDTH / 2 - defeat.getWidth() / 2);
        defeat.setY(-defeat.getHeight());


        //游戏基础界面设置
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        //获取主面板容器并对其添加元素/监听器
        Container c = getContentPane();
        c.setFocusable(true);
        c.add(hb);
        c.addKeyListener(new KeyAd());
        c.addMouseListener(new MouseAd());
        c.addMouseMotionListener(new MouseAd());

        //设置可见
        setVisible(true);
    }

    class KeyAd extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (state == GameState.GAMING) {
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
                return;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (state == GameState.GAMING) {
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
                return;
            }
        }
    }

    class MouseAd extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (state == GameState.WELCOME){
                //判断是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    System.exit(0);
                }
            }

            if (state == GameState.GAME_SELECT){
                //判断是否点击到开始按钮
                if (startGame.getHurtArea().contains(e.getX(),e.getY())){
                    state = GameState.GAMING;
                }

                //判断是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    System.exit(0);
                }

            }

            if (state == GameState.OVER){
                //是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    System.exit(0);
                }

                //是否点击到重玩按钮
                if (resume.getHurtArea().contains(e.getX(),e.getY())){
                    state = GameState.GAMING;
                    myPlane = new MyPlane();
                    enemyPlanes.clear();
                    enemyBullets.clear();
                    booms.clear();
                    meteors.clear();
                    defeat.setY(-defeat.getHeight());

                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (state == GameState.WELCOME){
                //退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    exit.setImage(Medias.getImage("exit_click.png"));
                }else{
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }

            if (state == GameState.GAME_SELECT){
                //开始按钮
                if (startGame.getHurtArea().contains(e.getX(),e.getY())){
                    startGame.setImage(Medias.getImage("btn_play_click.png"));
                }else {
                    startGame.setImage(Medias.getImage("btn_play.png"));
                }

                //退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    exit.setImage(Medias.getImage("exit_click.png"));
                }else{
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }

            if (state == GameState.OVER){
                //退出按钮
                if (exit.getHurtArea().contains(e.getX(),e.getY())){
                    exit.setImage(Medias.getImage("exit_click.png"));
                }else{
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }
        }
    }

    class HB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

            if (state == GameState.WELCOME || state == GameState.GAME_SELECT){
                g.drawImage(Medias.getImage("startbg.jpg"),0,0,this);
                drawGameModel(g,startGame);
                drawGameModel(g,exit);
                return;
            }

            if (state == GameState.GAMING) {
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

                //画玩家飞机的血条
                drawMyBlood(g);

                //画敌机子弹
                drawEnemyBullet(g);
                return;
            }

            if (state == GameState.OVER){
                g.drawImage(Medias.getImage("startbg.jpg"),0,0,this);
                drawGameModel(g,defeat);
                drawGameModel(g,exit);
                drawGameModel(g,resume);
            }

        }

        private void drawEnemyBullet(Graphics g) {
            for (int i = 0; i < enemyBullets.size(); i++) {
                drawGameModel(g, enemyBullets.get(i));
            }
        }

        private void drawMyBlood(Graphics g) {
            g.setColor(Color.RED);
            g.draw3DRect(
                    20,
                    Constants.WINDOW_HEIGHT - 50 - Constants.HP_HEIGHT,
                    Constants.WINDOW_WIDTH / 3,
                    Constants.HP_HEIGHT,
                    true
            );
            g.fill3DRect(
                    20,
                    Constants.WINDOW_HEIGHT - 50 - Constants.HP_HEIGHT,
                    (int) (myPlane.getHp() * 1.0 / myPlane.getMaxHp() * Constants.WINDOW_WIDTH / 3),
                    Constants.HP_HEIGHT,
                    true
            );

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

            if(state == GameState.WELCOME){
                startGame.setY(startGame.getY() - 5);
                if(startGame.getY() < 300){
                    state = GameState.GAME_SELECT;
                }

            }


            if (state == GameState.GAMING) {
                map.move();
                generateMyBullet();
                moveBullet();
                moveMyPlane();
                generateEnemyPlane();
                moveEnemyPlane();
                generateMeteor();
                moveMeteor();
                generateEnemyBullet();
                moveEnemyBullet();
            }

            if (state == GameState.OVER){
                if(defeat.getY() < 250){
                    defeat.setY(defeat.getY() + 3);
                }
            }

            hb.repaint();



            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveEnemyBullet() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet bullet = enemyBullets.get(i);
            if (bullet.moveDown() == false) {
                enemyBullets.remove(i);
                i--;
                continue;
            }

            if (myPlane.getHurtArea().intersects(bullet.getHurtArea())) {
                myPlane.setHp(myPlane.getHp() - bullet.getAttack());
                if (myPlane.getHp() <= 0) {
                    state = GameState.OVER;
                }

                enemyBullets.remove(i);
                i--;

            }


        }
    }

    private void generateEnemyBullet() {
        enemyBulletGenerateInterval++;
        if (enemyBulletGenerateInterval > 50) {
            for (int i = 0; i < enemyPlanes.size(); i++) {
                Factory.generateBullet(enemyPlanes.get(i), enemyBullets, BulletType.ENEMY);
            }
            enemyBulletGenerateInterval = 0;
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
        meteortGenerateInterval++;
        if (meteortGenerateInterval > 40) {
            Factory.generateMeteor(meteors);
            meteortGenerateInterval = 0;
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

    private void addBoom(GameModel gameModel) {
        BOOM boom = new BOOM();
        boom.setX(gameModel.getX());
        boom.setY(gameModel.getY());
        booms.add(boom);
    }

    private void generateMyBullet() {
        generateBulletInterval++;
        if (generateBulletInterval >= 5) {
            Factory.generateBullet(myPlane, myPlane.getMyBullets(), BulletType.NORMAL);
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
