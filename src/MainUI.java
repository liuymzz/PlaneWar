import entity.GameMap;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainUI extends JFrame implements Runnable{
    private HB hb = new HB();
    private GameMap map = new GameMap();
    int i = 0;

    public MainUI(){
        setSize(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        Container c = getContentPane();
        c.add(hb);

        setVisible(true);
    }



    class HB extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            drawMap(g);
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

            i++;
            if(i == 120){
                map.changeMapNum();
                i = 0;
            }

            hb.repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        MainUI ui = new MainUI();
        new Thread(ui).start();
    }
}
