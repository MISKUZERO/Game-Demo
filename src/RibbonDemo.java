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
        g.drawImage(bg, xbg, -height, this);
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, 20, 35);
    }

    public RibbonDemo() {
        height = 600;
        width = 800;
        x = 100;
        y = height - 68;
        bg = getToolkit().createImage("E:\\我的收藏\\美图\\wallhaven-8ogjek.jpg");
        playAnimation();
    }

    private void playAnimation() {
        new Thread(() -> {
            final int lBound = 50, rBound = width - 100;//窗口边界
            final int blBound = width - 1600, brBound = 0;//卷轴（背景图）边界
            final int dBound = y;//下边界
            final long delay = 1;//延迟（与帧率成反比）
            final double g = 10000;//重力加速度（pixel/s^2）
            final double a = 2000;//右移动加速度（pixel/s^2）
            final double vJump = -2000;//跳跃速度（pixel/s）
            final double vxMax = 500;//最大速率（pixel/s）
            final double deltaT = (double) delay / 1000;
            final double factor = 0.2;//摩擦系数
            double xl = x;//左位移（pixel）
            double xr = x;//右位移（pixel）
            double xm = x;//不位移（pixel）
            double y = dBound;//高度位移（pixel）
            double vx = 0;//x速度（pixel/s）
            double vy = -vJump;//y速度（pixel/s）
            while (true) {
                //确定横向参数
                if (lMove && !rMove) {//仅按A键
                    if (this.y == dBound) {//在地上
                        vx -= a * deltaT;
                        if (vx > vxMax)
                            vx = vxMax;
                        else if (vx < -vxMax)
                            vx = -vxMax;
                    }
                    x = (int) (xr = xm = xl += vx * deltaT);
                } else if (rMove && !lMove) {//仅按D键
                    if (this.y == dBound) {//在地上
                        vx += a * deltaT;
                        if (vx > vxMax)
                            vx = vxMax;
                        else if (vx < -vxMax)
                            vx = -vxMax;
                    }
                    x = (int) (xl = xm = xr += vx * deltaT);
                } else {
                    if (vx > factor)
                        vx -= factor;
                    else if (vx < -factor)
                        vx += factor;
                    else
                        vx = 0;
                    x = (int) (xl = xr = xm += vx * deltaT);
                }
                //确定纵向参数
                if (this.y == dBound) {//在地上
                    if (jump)
                        vy = vJump;
                } else
                    vy += g * deltaT;
                y += vy * deltaT;
                if (y > dBound)
                    y = dBound;
                this.y = (int) y;
                //调试
                System.out.print("\rx: " + x + ", y: " + this.y + ", vx: " + vx + ", vy: " + vy);
                //渲染
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
