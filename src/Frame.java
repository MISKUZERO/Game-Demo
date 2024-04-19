import component.RectSprite;
import controller.Reality;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Frame extends JPanel {

    private final int height;
    private final int width;

    private final RectSprite hero;
    private final RectSprite sprite1;
    private final RectSprite sprite2;
    private final RectSprite sprite3;

    private volatile boolean lMove;
    private volatile boolean rMove;
    private volatile boolean jump;

    private final Reality reality;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawLine(0, 500, 2000, 500);
        g.setColor(Color.BLUE);
        drawSpriteBox(g, hero);
        drawSpriteBox(g, sprite1);
        drawSpriteBox(g, sprite2);
        drawSpriteBox(g, sprite3);
    }

    public Frame() {
        height = 600;
        width = 1800;
        hero = new RectSprite(100, 0, 20, 50, 50) {
        };
        sprite1 = new RectSprite(200, 0, 30, 15, 60) {
        };
        sprite2 = new RectSprite(400, 0, 20, 60, 400) {
        };
        sprite3 = new RectSprite(500, 0, 10, 35, 5) {
        };
        reality = new Reality();
    }

    private static void drawSpriteBox(Graphics g, RectSprite sprite) {
        g.drawRect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }


    private void playAnimation(RectSprite... sprites) {
        new Thread(() -> {
            while (true) {
                reality.updateControllable(hero, lMove, rMove, jump);
                for (int i = 1; i < sprites.length; i++)
                    reality.updateUncontrollable(sprites[i]);
                reality.updateAllColliders(sprites);
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
        playAnimation(hero, sprite1, sprite2, sprite3);
    }


    public static void main(String[] args) {
        new Frame().start();
    }

}
