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
import utils.SoundUtils;

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

    private int pauseInterval = 0;                                          //暂停闪烁间隔

    private int score = 0;                                                  //飞行成绩
    private int destoryEnemyPlaneNum = 0;                                   //击毁敌机数量

    private int mapRecord = 0;                                              //记录飞行距离，用来确定boss出现
    private EnemyPlane BOSS = null;                                         //BOSS

    private List<EnemyPlane> myPlanes = new ArrayList<>();                  //道具2使用
    private boolean DJ2 = false;                                            //用于判断2键是否被按下，用来执行道具2的操作

    private boolean IsTheBest = false;                                      //用来判断是否获得历史最佳

    private int DEADtime = 0;                                               //死亡时间，展示死亡后战机爆炸的效果
    private int addBOOMSInterval = 0;                                        //死亡时间产生爆炸效果的时间间隔


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

    public static void main(String[] args) {
        MainUI ui = new MainUI();
        new Thread(ui).start();
    }

    @Override
    public void run() {
        while (1 == 1) {

            if (state == GameState.WELCOME) {
                startGame.setY(startGame.getY() - 5);
                if (startGame.getY() < 300) {
                    state = GameState.GAME_SELECT;
                }

            }


            if (state == GameState.GAMING) {
                map.move();                     //地图移动
                score++;                       //成绩增加
                mapRecord++;                   //控制boss出现的时间点
                generateMyBullet();             //玩家飞机发射子弹
                moveBullet();                   //移动玩家子弹
                moveMyPlane();                  //玩家飞机移动的控制
                generateEnemyPlane();           //产生敌机
                moveEnemyPlane();               //移动敌机
                generateMeteor();               //产生陨石
                moveMeteor();                   //移动陨石
                generateEnemyBullet();          //产生敌机子弹
                moveEnemyBullet();              //移动敌机子弹

                generateMyPlanes();             //道具2中战机的产生
                moveMyPlanes();                 //道具2中的战机的移动
                DJ2();                          //道具2对敌机造成伤害的操作

                if (mapRecord > 1000) {           //到了特定的时间点产生boss
                    map.changeMapNum();
                    mapRecord = 0;
                }
            }

            if (state == GameState.DEAD) {
                DEADtime ++;
                map.move();
                moveEnemyPlane();
                moveEnemyBullet();
                moveMeteor();
                moveMyPlanes();
                if (DEADtime > 500) {
                    state = GameState.OVER;
                    SoundUtils.Play(Medias.getAudios("game_over.wav"), false);
                    DEADtime = 0;
                }
                if (DEADtime < 400) {
                    addBOOMs();
                }
            }

            if (state == GameState.OVER) {
                if (defeat.getY() < 250) {
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

    private void addBOOMs() {
        addBOOMSInterval ++;

        if (addBOOMSInterval > 30) {
            BOOM boom = new BOOM();
            boom.setX(Factory.random(myPlane.getX() + myPlane.getWidth() + 100 - (myPlane.getX() - 100)) + myPlane.getX() - 100);
            boom.setY(Factory.random(myPlane.getY() + myPlane.getHeight() + 100 - (myPlane.getY() - 100)) + myPlane.getY() - 100);
            booms.add(boom);
            SoundUtils.Play(Medias.getAudios("explosion_enemy.wav"), false);
            addBOOMSInterval = 0;

        }
    }

    private void DJ2() {
        for (int j = 0; j < myPlanes.size(); j++) {
            if (myPlanes.get(j).getY() < Constants.WINDOW_HEIGHT / 2) {
                for (int i = 0; i < enemyPlanes.size(); i++) {
                    addBoom(enemyPlanes.get(i));
                    enemyPlanes.remove(i);
                    destoryEnemyPlaneNum++;
                    i--;
                }
            }
        }
    }

    private void generateMyPlanes() {
        if (DJ2 == true) {
            Factory.generateMyPlanes(myPlanes);
        }
    }

    private void moveMyPlanes() {
        EnemyPlane mp = null;
        for (int i = 0; i < myPlanes.size(); i++) {
            mp = myPlanes.get(i);
            if (mp.move() == false) {
                myPlanes.remove(mp);
                i--;
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
                    state = GameState.DEAD;
                    zuijia();                          //判断是否获得历史最佳
                }

                enemyBullets.remove(i);
                i--;

            }


        }
    }

    //用来判断是否获得历史最佳
    private void zuijia() {
        int mileage = Factory.getMaxScore("mileage");
        int destory_enemys = Factory.getMaxScore("destory_enemys");
        if (score > mileage) {
            Factory.setMaxScore(score, "mileage");
            IsTheBest = true;
        }
        if (destoryEnemyPlaneNum > destory_enemys) {
            Factory.setMaxScore(destoryEnemyPlaneNum, "destory_enemys");
            IsTheBest = true;
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
                continue;
            }

            if (meteor.getHurtArea().intersects(myPlane.getHurtArea())) {
                meteors.remove(meteor);
                addBoom(myPlane);
                i--;
                myPlane.setHp(myPlane.getHp() / 2);
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
                        //回蓝
                        myPlane.setEnergy(myPlane.getEnergy() + 100);
                        if (myPlane.getEnergy() > myPlane.getMaxEnergy()) {
                            myPlane.setEnergy(myPlane.getMaxEnergy());
                        }

                        destoryEnemyPlaneNum++;
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
                            //回蓝
                            myPlane.setEnergy(myPlane.getEnergy() + 200);
                            if (myPlane.getEnergy() > myPlane.getMaxEnergy()) {
                                myPlane.setEnergy(myPlane.getMaxEnergy());
                            }

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
        boom.setX(gameModel.getX() + gameModel.getWidth() / 2 - boom.getImage().getWidth() / boom.getMaxIndex() / 2);
        boom.setY(gameModel.getY());
        booms.add(boom);
        if (gameModel instanceof EnemyPlane) {
            SoundUtils.Play(Medias.getAudios("explosion_enemy.wav"), false);
        }
        if (gameModel instanceof Meteor) {
            SoundUtils.Play(Medias.getAudios("explosion_asteroid.wav"), false);
        }
    }

    private void generateMyBullet() {
        generateBulletInterval++;
        if (generateBulletInterval >= 5) {
            Factory.generateBullet(myPlane, myPlane.getMyBullets(), BulletType.NORMAL);
            generateBulletInterval = 0;
            SoundUtils.Play(Medias.getAudios("bullet.wav"), false);                 //播放发射子弹的声音

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
                    case KeyEvent.VK_P:
                        state = GameState.PAUSE;
                    case KeyEvent.VK_2:
                        DJ2_VK();
                        break;
                }

                if (e.getKeyCode() == KeyEvent.VK_1) {
                    if (myPlane.getEnergy() > 100) {
                        myPlane.setEnergy(myPlane.getEnergy() - 100);
                        myPlane.setHp(myPlane.getHp() + myPlane.getMaxHp() / 3);
                        if (myPlane.getHp() > myPlane.getMaxHp()) {
                            myPlane.setHp(myPlane.getMaxHp());
                        }
                    }
                }
                return;
            }

            if (state == GameState.PAUSE) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        state = GameState.GAMING;
                        break;
                }
            }

        }

        private void DJ2_VK() {
            if (myPlane.getHp() > myPlane.getMaxEnergy() / 2) {
                myPlane.setEnergy(0);
                DJ2 = true;
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
                    case KeyEvent.VK_2:
                        DJ2 = false;
                        break;
                }
                return;
            }
        }
    }

    class MouseAd extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            SoundUtils.Play(Medias.getAudios("click.wav"), false);
            if (state == GameState.WELCOME) {
                //判断是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }
            }

            if (state == GameState.GAME_SELECT) {
                //判断是否点击到开始按钮
                if (startGame.getHurtArea().contains(e.getX(), e.getY())) {
                    state = GameState.GAMING;
                }

                //判断是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }

            }

            if (state == GameState.OVER) {
                //是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }

                //是否点击到重玩按钮
                if (resume.getHurtArea().contains(e.getX(), e.getY())) {
                    resume();
                }
            }

            if (state == GameState.PAUSE) {
                //是否点击到退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    System.exit(0);
                }

                //是否点击到重玩按钮
                if (resume.getHurtArea().contains(e.getX(), e.getY())) {
                    resume();
                }
            }


        }

        //重新游戏
        private void resume() {
            state = GameState.GAMING;
            myPlane = new MyPlane();
            enemyPlanes.clear();
            enemyBullets.clear();
            booms.clear();
            meteors.clear();
            score = 0;
            destoryEnemyPlaneNum = 0;
            defeat.setY(-defeat.getHeight());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (state == GameState.WELCOME) {
                //退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    exit.setImage(Medias.getImage("exit_click.png"));
                } else {
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }

            if (state == GameState.GAME_SELECT) {
                //开始按钮
                if (startGame.getHurtArea().contains(e.getX(), e.getY())) {
                    startGame.setImage(Medias.getImage("btn_play_click.png"));
                } else {
                    startGame.setImage(Medias.getImage("btn_play.png"));
                }

                //退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    exit.setImage(Medias.getImage("exit_click.png"));
                } else {
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }

            if (state == GameState.OVER) {
                //退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    exit.setImage(Medias.getImage("exit_click.png"));
                } else {
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }

            if (state == GameState.PAUSE) {
                //退出按钮
                if (exit.getHurtArea().contains(e.getX(), e.getY())) {
                    exit.setImage(Medias.getImage("exit_click.png"));
                } else {
                    exit.setImage(Medias.getImage("exit.png"));
                }
            }
        }
    }

    class HB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

            if (state == GameState.WELCOME || state == GameState.GAME_SELECT) {
                g.drawImage(Medias.getImage("startbg.jpg"), 0, 0, this);
                drawGameModel(g, startGame);
                drawGameModel(g, exit);
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

                //画玩家的能量条
                drawMyEnergy(g);

                //画敌机子弹
                drawEnemyBullet(g);

                //画成绩
                drawAchievement(g);

                //画道具2
                drawDJ2(g);

                return;
            }

            if (state == GameState.DEAD){
                drawMap(g);
                //画敌机
                drawEnemyPlanes(g);

                //画爆炸效果
                drawBoom(g);

                //画陨石
                drawMeteor(g);

                //画玩家飞机的血条
                drawMyBlood(g);

                //画玩家的能量条
                drawMyEnergy(g);

                //画敌机子弹
                drawEnemyBullet(g);

                return;
            }

            if (state == GameState.OVER) {
                g.drawImage(Medias.getImage("startbg.jpg"), 0, 0, this);
                drawGameModel(g, defeat);
                drawGameModel(g, exit);
                drawGameModel(g, resume);
                drawOverAchievement(g);
                drawHistory(g);                     //绘制历史
                return;
            }

            if (state == GameState.PAUSE) {
                BufferedImage pause = Medias.getImage("pause_head.png");
                g.drawImage(Medias.getImage("startbg.jpg"), 0, 0, this);
                pauseInterval++;
                if (pauseInterval > 40) {
                    g.drawImage(pause, Constants.WINDOW_WIDTH / 2 - pause.getWidth() / 2, 300, this);
                    if (pauseInterval > 80) {
                        pauseInterval = 0;
                    }
                }
                drawGameModel(g, resume);
                drawGameModel(g, exit);
                return;
            }

        }

        private void drawHistory(Graphics g) {
            g.drawString("历史最佳：", 150, 630);
            g.drawString("飞行距离：" + Factory.getMaxScore("mileage"), 150, 650);
            g.drawString("击毁敌机：" + Factory.getMaxScore("destory_enemys"), 150, 670);
            if (IsTheBest) {
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
                g.drawString("恭喜您是历史最佳！", 150, 690);
            }
        }

        private void drawDJ2(Graphics g) {
            for (int i = 0; i < myPlanes.size(); i++) {
                drawGameModel(g, myPlanes.get(i));
            }
        }

        private void drawOverAchievement(Graphics g) {
            Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
            g.setFont(font);
            g.drawString("本次成绩：", 150, 550);
            g.drawString("飞行距离" + score, 150, 570);
            g.drawString("击毁敌机" + destoryEnemyPlaneNum + "架", 150, 590);
        }

        private void drawMyEnergy(Graphics g) {
            g.setColor(Color.BLUE);
            g.draw3DRect(
                    20,
                    Constants.WINDOW_HEIGHT - 50 - Constants.HP_HEIGHT - Constants.HP_HEIGHT * 2,
                    Constants.WINDOW_WIDTH / 3,
                    Constants.HP_HEIGHT,
                    true
            );
            g.fill3DRect(
                    20,
                    Constants.WINDOW_HEIGHT - 50 - Constants.HP_HEIGHT - Constants.HP_HEIGHT * 2,
                    (int) (myPlane.getEnergy() * 1.0 / myPlane.getMaxEnergy() * Constants.WINDOW_WIDTH / 3),
                    Constants.HP_HEIGHT,
                    true
            );

        }

        private void drawAchievement(Graphics g) {
            Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
            g.setFont(font);
            g.setColor(Color.red);
            g.drawString("您的飞行距离" + score, 300, 50);
            g.drawString("击毁敌机" + destoryEnemyPlaneNum + "架", 300, 70);
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

}
