import component.RectActor;
import controller.ArgsController;
import controller.Reality;
import util.CollideTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Frame extends JPanel {

    private final int height;
    private final int width;
    private final Image bg;

    private final RectActor actor1;
    private final RectActor actor2;

    private volatile boolean lMove;
    private volatile boolean rMove;
    private volatile boolean jump;

    private final ArgsController argsController;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, -height, this);
        g.setColor(Color.YELLOW);
        drawActorBox(g, actor1);
        drawActorBox(g, actor2);
    }

    public Frame() {
        height = 600;
        width = 800;
        bg = getToolkit().createImage("E:\\我的收藏\\美图\\wallhaven-8ogjek.jpg");
        actor1 = new RectActor(20, 35, 100, 0, 60) {
        };
        actor2 = new RectActor(30, 35, 500, 0, 50) {
        };
        argsController = new Reality();
    }

    private static void drawActorBox(Graphics g, RectActor actor) {
        g.drawRect((int) actor.getX(), (int) actor.getY(), actor.getWidth(), actor.getHeight());
    }


    private void playAnimation(RectActor... actors) {
        new Thread(() -> {
            while (true) {
                argsController.updateControllable(actors[0], lMove, rMove, jump);
                for (int i = 1; i < actors.length; i++)
                    argsController.updateUncontrollable(actors[i]);
                render();
            }
        }).start();
    }

    private void render() {
        repaint();
        try {
            //帧生成延迟（与帧率成反比）
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A)
                    lMove = true;
                else if (keyCode == KeyEvent.VK_D)
                    rMove = true;
                else if (keyCode == KeyEvent.VK_SPACE)
                    jump = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A)
                    lMove = false;
                else if (keyCode == KeyEvent.VK_D)
                    rMove = false;
                else if (keyCode == KeyEvent.VK_SPACE)
                    jump = false;
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        playAnimation(actor1, actor2);
    }


    public static void main(String[] args) {
        new Frame().start();
    }

}
