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
            final double A = 5;//加速度常量（pixel/s^2）
            long t0 = System.currentTimeMillis();//系统时间（ms）
            int x = this.x;//位移（pixel）
            double v = 0;//速度（pixel/s）
            double a;//加速度（pixel/s^2）
            while (true) {
                double t = (double) timeAt(t0) / 1000;//运动时间（s）
                if (lMove && !rMove)//仅按A键
                    a = -A;
                else if (rMove && !lMove)//仅按D键
                    a = A;
                else {
                    t0 = System.currentTimeMillis();
                    x = this.x;
//                    if (v > 1)
//                        v--;
//                    else if (v < -1)
//                        v++;
//                    else
                    v = 0;
                    a = 0;
                }
                if (t < 0.5)//加速时长
                    v = v + a * t;
                this.x = (int) (x + v * t);
                System.out.print("\rt: " + t + ",x: " + this.x + ",v: " + v + ",a: " + a);
//                if (jump) ;
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
