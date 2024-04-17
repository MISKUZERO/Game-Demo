import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RibbonDemo extends JPanel {

    private final int height;
    private final int width;
    private final Image bg;
    private int bX;
    private volatile int x;
    private volatile int y;
    private volatile boolean _xMove;
    private volatile boolean xMove;
    private volatile boolean _yMove;
    private volatile boolean yMove;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawImage(bg, bX, -height, this);
        g.drawRect(x, y, 50, 50);
    }

    public RibbonDemo() {
        height = 600;
        width = 800;
        x = 100;
        y = height - 80;
        bg = getToolkit().createImage("E:\\我的收藏\\美图\\wallhaven-8ogjek.jpg");
        playAnimation();
    }

    private static long time(long start) {
        return System.currentTimeMillis() - start;
    }

    private void playAnimation() {
        new Thread(() -> {
            final int lBound = 50, rBound = width - 100;//窗口边界
            final int blBound = width - 1600, brBound = 0;//卷轴（背景图）边界
            int v = 500;//速度（pixel/s）
            long _x0 = x, x0 = x;
            long _tx0 = System.currentTimeMillis(), tx0 = System.currentTimeMillis();
            long _tx = 0, tx = 0;
            while (true) {
                System.out.print("\r_tx: " + _tx + ", tx: " + tx);
                if (_xMove) {
                    _tx = time(_tx0);
                    x = (int) (_x0 - (double) v * _tx / 1000);
                } else {
                    _x0 = x;
                    _tx0 = System.currentTimeMillis();
                    _tx = 0;
                }
                if (xMove) {
                    tx = time(tx0);
                    x = (int) (x0 + (double) v * tx / 1000);
                } else {
                    x0 = x;
                    tx0 = System.currentTimeMillis();
                    tx = 0;
                }
                repaint();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void start() {
        JFrame frame = new JFrame("RibbonDemo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A)
                    _xMove = true;
                else if (keyCode == KeyEvent.VK_D)
                    xMove = true;
                else if (keyCode == KeyEvent.VK_W)
                    _yMove = true;
                else if (keyCode == KeyEvent.VK_S)
                    yMove = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A)
                    _xMove = false;
                else if (keyCode == KeyEvent.VK_D)
                    xMove = false;
                else if (keyCode == KeyEvent.VK_W)
                    _yMove = false;
                else if (keyCode == KeyEvent.VK_S)
                    yMove = false;
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new RibbonDemo().start();
    }

}
