import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RibbonDemo extends JPanel {

    private final int height;
    private final int width;
    private final Image bg;
    private int bX;
    private int x;
    private int y;
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

    private void playAnimation() {
        new Thread(() -> {
            int step = 1;
            int lBound = 50;
            int rBound = width - 100;
            int blBound = width - 1600;
            int brBound = 0;
            while (true) {
                if (_xMove)
                    if (x > lBound)
                        x -= step;
                    else if (bX < brBound)
                        bX += step;
                if (xMove)
                    if (x < rBound)
                        x += step;
                    else if (bX > blBound)
                        bX -= step;
                if (_yMove)
                    y -= step;
                if (yMove)
                    y += step;
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
