import component.RectSprite;
import component.RectUnit;
import controller.Reality;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Frame extends JPanel {

    private final int height;
    private final int width;

    private final RectUnit ground;
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
        drawUnitBox(g, ground);
        g.setColor(Color.BLUE);
        drawUnitBox(g, hero);
        drawUnitBox(g, sprite1);
        drawUnitBox(g, sprite2);
        drawUnitBox(g, sprite3);
    }

    public Frame() {
        height = 600;
        width = 1800;
        ground = new RectUnit(-500, 400, 2000, 500) {
        };
        hero = new RectSprite(100, 0, 20, 50, 50) {
        };
        sprite1 = new RectSprite(200, 0, 300, 15, 60) {
        };
        sprite2 = new RectSprite(400, 0, 20, 60, 400) {
        };
        sprite3 = new RectSprite(500, 0, 10, 35, 5) {
        };
        reality = new Reality();
    }

    private static void drawUnitBox(Graphics g, RectUnit unit) {
        g.drawRect(unit.getX(), unit.getY(), unit.getWidth(), unit.getHeight());
    }


    private void playAnimation() {
        new Thread(() -> {
            final RectUnit ground = this.ground;
            final RectSprite hero = this.hero;
            final RectSprite sprite1 = this.sprite1;
            final RectSprite sprite2 = this.sprite2;
            final RectSprite sprite3 = this.sprite3;
            final Reality reality = this.reality;
            while (true) {
                reality.updateControllable(hero, lMove, rMove, jump);
                reality.updateUncontrollable(sprite1);
                reality.updateUncontrollable(sprite2);
                reality.updateUncontrollable(sprite3);
                Reality.updateAllColliders(20, 5, 5, ground, hero, sprite1, sprite2, sprite3);
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
        playAnimation();
    }


    public static void main(String[] args) {
        new Frame().start();
    }

}
