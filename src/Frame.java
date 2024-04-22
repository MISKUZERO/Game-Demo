import component.RectSprite;
import component.RectUnit;
import controller.Reality;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Frame extends JPanel {

    private final int height;
    private final int width;

    private final RectUnit ground;
    private final RectSprite hero;
    private final RectSprite sprite1;
    private final RectSprite sprite2;
    private final RectSprite sprite3;
    private final LinkedList<RectUnit> units;

    private volatile boolean lMove;
    private volatile boolean rMove;
    private volatile boolean jump;
    private volatile boolean shoot;
    private volatile boolean burst;
    private volatile boolean create;
    private volatile boolean ctlAndB2Mask;
    private volatile int x0;
    private volatile int y0;
    private volatile int x;
    private volatile int y;

    private final Reality reality;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if (ctlAndB2Mask) {
            int x0 = this.x0, y0 = this.y0;
            int x = this.x, y = this.y;
            if (x0 < x)
                if (y0 < y)
                    g.drawRect(x0, y0, x - x0, y - y0);
                else
                    g.drawRect(x0, y, x - x0, y0 - y);
            else if (y0 < y)
                g.drawRect(x, y0, x0 - x, y - y0);
            else
                g.drawRect(x, y, x0 - x, y0 - y);
        }
        for (RectUnit unit : units)
            drawUnitBox(g, unit);
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
        units = new LinkedList<>();
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
            final LinkedList<RectUnit> units = this.units;
            final Reality reality = this.reality;
            units.add(ground);
            units.add(hero);
            units.add(sprite1);
            units.add(sprite2);
            units.add(sprite3);
            int spritesIndex = 1;
            int t = 0;
            int dT = 10;
            while (true) {
                //鼠标检测
                if (create) {
                    int x0 = this.x0, y0 = this.y0;
                    int x = this.x, y = this.y;
                    if (x0 < x)
                        if (y0 < y)
                            units.add(spritesIndex++, new RectUnit(x0, y0, x - x0, y - y0) {
                            });
                        else
                            units.add(spritesIndex++, new RectUnit(x0, y, x - x0, y0 - y) {
                            });
                    else if (y0 < y)
                        units.add(spritesIndex++, new RectUnit(x, y0, x0 - x, y - y0) {
                        });
                    else
                        units.add(spritesIndex++, new RectUnit(x, y, x0 - x, y0 - y) {
                        });
                    create = false;
                } else if (shoot) {
                    shoot(hero, units, reality, 5, 5, 1, 100);
                    shoot = false;
                } else if (burst && t++ % dT == 0)
                    shoot(hero, units, reality, 2, 2, 0.5, 50);
                //精灵参数更新
                reality.updateControllable(hero, lMove, rMove, jump);
                int len = units.size();
                for (int i = spritesIndex + 1; i < len; i++)
                    reality.updateUncontrollable((RectSprite) units.get(i));
                Reality.updateAllColliders(20, 5, 5, units.toArray(new RectUnit[0]));
                if (units.size() > 1000)
                    units.remove(spritesIndex + 4);
                render();
            }
        }).start();
    }

    private void shoot(RectSprite hero, LinkedList<RectUnit> units, Reality reality, int width, int height, double m, int speed) {
        double x = hero.getExactX();
        double y = hero.getExactY();
        RectSprite b = new RectSprite(x, y - 20, width, height, m) {
        };
        b.setVxAndVy(speed * (this.x - x), speed * (this.y - y));
        units.add(b);
        reality.updateUncontrollable(b);
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
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int modifiersEx = e.getModifiersEx();
                if (modifiersEx == InputEvent.BUTTON1_DOWN_MASK) {
                    x = e.getX();
                    y = e.getY();
                    shoot = true;
                } else if (modifiersEx == InputEvent.BUTTON3_DOWN_MASK) {
                    x = e.getX();
                    y = e.getY();
                    burst = true;
                } else if (modifiersEx == 4224) {//Ctrl+右键
                    x0 = e.getX();
                    y0 = e.getY();
                    x = e.getX();
                    y = e.getY();
                    ctlAndB2Mask = true;
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int modifiers = e.getModifiers();
                if (modifiers == 4)
                    burst = false;
                else if (ctlAndB2Mask) {
                    create = true;
                    ctlAndB2Mask = false;
                }
            }
        });
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
