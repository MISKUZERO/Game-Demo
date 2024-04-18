import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RibbonDemo extends JPanel {

    private final int height;
    private final int width;
    private final Image bg;

    private final int delay = 1;//帧生成延迟（与帧率成反比）

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
        g.drawImage(bg, 0, -height, this);
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, 20, 35);
    }

    public RibbonDemo() {
        height = 600;
        width = 800;
        x = 100;
        y = height - 68;
        bg = getToolkit().createImage("E:\\我的收藏\\美图\\wallhaven-8ogjek.jpg");
    }

    private void playAnimation() {
        new Thread(() -> {
            final int ground = y;//地面高度
            final double deltaT = (double) delay / 1000;//时间间隔（s）
            final double friction = 0.2;//摩擦系数
            final double gravity = 10000;//重力加速度（pixel/s^2）
            final double acceleration = 2000;//（右）奔跑加速度（pixel/s^2）
            final double vRunMax = 500;//最大奔跑速率（pixel/s）
            final double vJump = -2000;//跳跃速度（pixel/s）

            double xl = x;//左位移记录（pixel）
            double xr = x;//右位移记录（pixel）
            double xm = x;//保持位移记录（pixel）
            double y = ground;//高度位移记录（pixel）
            double vx = 0;//x方向速度（pixel/s）
            double vy = -vJump;//y方向速度（pixel/s）
            while (true) {
                //确定横向参数
                if (lMove && !rMove) {//仅按A键
                    if (this.y == ground) {//在地上
                        vx -= acceleration * deltaT;
                        if (vx > vRunMax)
                            vx = vRunMax;
                        else if (vx < -vRunMax)
                            vx = -vRunMax;
                    }
                    x = (int) (xr = xm = xl += vx * deltaT);
                } else if (rMove && !lMove) {//仅按D键
                    if (this.y == ground) {//在地上
                        vx += acceleration * deltaT;
                        if (vx > vRunMax)
                            vx = vRunMax;
                        else if (vx < -vRunMax)
                            vx = -vRunMax;
                    }
                    x = (int) (xl = xm = xr += vx * deltaT);
                } else {
                    if (vx > friction)
                        vx -= friction;
                    else if (vx < -friction)
                        vx += friction;
                    else
                        vx = 0;
                    x = (int) (xl = xr = xm += vx * deltaT);
                }
                //确定纵向参数
                if (this.y == ground) {//在地上
                    if (jump)
                        vy = vJump;
                } else
                    vy += gravity * deltaT;
                y += vy * deltaT;
                if (y > ground)
                    y = ground;
                this.y = (int) y;
                //调试
                System.out.print("\rx: " + x + ", y: " + this.y + ", vx: " + vx + ", vy: " + vy);
                //渲染
                render();
            }
        }).start();
    }

    private void render() {
        repaint();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        playAnimation();
    }


    public static void main(String[] args) {
        new RibbonDemo().start();
    }

}
