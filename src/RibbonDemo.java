import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RibbonDemo extends JPanel {

    private final int height;
    private final int width;
    private final Image bg;
    private volatile int xbg;//背景的横坐标
    private volatile int x;
    private volatile int y;
    private volatile boolean lMove;
    private volatile boolean rMove;
    private volatile boolean jump;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawImage(bg, xbg, -height, this);
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

    private static long timeAt(long start) {
        return System.currentTimeMillis() - start;
    }

    private void playAnimation() {
        new Thread(() -> {
            final int lBound = 50, rBound = width - 100;//窗口边界
            final int blBound = width - 1600, brBound = 0;//卷轴（背景图）边界
            final double a = 500;//加速度常量（pixel/s^2）
            final double maxV = 500;//最大速率（pixel/s）
            final long delay = 1;
            final double deltaT = (double) delay / 1000;
            double v = 0;//速度（pixel/s）
            long tl0 = System.currentTimeMillis();//左加速时间（ms）
            long tr0 = System.currentTimeMillis();//右加速时间（ms）
            double t;//时间（ms）
            int xl = x;//左位移（pixel）
            int xr = x;//右位移（pixel）
            int xm = x;//不位移（pixel）
            while (true) {
                if (lMove && !rMove) {//仅按A键
                    t = (double) timeAt(tl0) / 1000;//左加速时间（s）
                    tr0 = System.currentTimeMillis();
                    v -= a * deltaT;
                    if (v > maxV)
                        v = maxV;
                    else if (v < -maxV)
                        v = -maxV;
                    x = xr = xm = (int) (xl + v * t);
                } else if (rMove && !lMove) {//仅按D键
                    t = (double) timeAt(tr0) / 1000;//右加速时间（s）
                    tl0 = System.currentTimeMillis();
                    v += a * deltaT;
                    if (v > maxV)
                        v = maxV;
                    else if (v < -maxV)
                        v = -maxV;
                    x = xl = xm = (int) (xr + v * t);
                } else {
                    tl0 = tr0 = System.currentTimeMillis();
//                    if (v > 1) {
//                        v--;
//                    } else if (v < -1) {
//                        v++;
//                    } else {
                    v = 0;
//                    }
                    x = xl = xr = xm;
                }
//                System.out.print("\rt: " + t + ",x: " + x + ",v: " + v);
//                if (jump) ;
                repaint();
                try {
                    Thread.sleep(delay);
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
    }


    public static void main(String[] args) {
        new RibbonDemo().start();
    }

}
