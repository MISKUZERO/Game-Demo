package controller;

import component.RectSprite;
import component.RectUnit;
import util.CollideTest;

public class Reality implements RectArgsController {

    private double dT;//时间间隔（s）
    private double m;//摩擦系数
    private double c;//空气阻力系数
    private double g;//重力加速度（pixel/s^2）
    private double a;//（右）奔跑加速度（pixel/s^2）
    private double vRMax;//奔跑最大速率（pixel/s）
    private double vJ;//跳跃速度（pixel/s）

    public Reality() {
        this(0.001, 0.2, 0.001, 10000, 1000, 500, -2000);
    }

    public Reality(double a, double vRMax, double vJ) {
        this(0.001, 0.2, 0.5, 10000, a, vRMax, vJ);
    }

    public Reality(double dT, double m, double c, double g, double a, double vRMax, double vJ) {
        this.dT = dT;
        this.m = m;
        this.c = c;
        this.g = g;
        this.a = a;
        this.vRMax = vRMax;
        this.vJ = vJ;
    }

    public double getDT() {
        return dT;
    }

    public void setDT(double dT) {
        this.dT = dT;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getVRMax() {
        return vRMax;
    }

    public void setVRMax(double vRMax) {
        this.vRMax = vRMax;
    }

    public double getVJ() {
        return vJ;
    }

    public void setVJ(double vJ) {
        this.vJ = vJ;
    }

    @Override
    public void updateControllable(RectSprite sprite, boolean... signals) {
        boolean lMove = signals[0], rMove = signals[1], jump = signals[2];//控制信号
        double dT = this.dT;
        double c = this.c;
        boolean land = sprite.isLand();
        //1.更新x方向上速度和位置
        if (land) {
            if (lMove && !rMove) {//左移动
                sprite.setVx(sprite.getVx() - a * dT);
                double vRMax = -this.vRMax;
                if (sprite.getVx() < vRMax)
                    sprite.setVx(vRMax);
            } else if (rMove && !lMove) {//右移动
                sprite.setVx(sprite.getVx() + a * dT);
                double vRMax = this.vRMax;
                if (sprite.getVx() > vRMax)
                    sprite.setVx(vRMax);
            } else {
                double vx = sprite.getVx();
                double m = this.m;
                if (vx > m)
                    sprite.setVx(sprite.getVx() - m);
                else if (vx < -m)
                    sprite.setVx(sprite.getVx() + m);
                else
                    sprite.setVx(0);
            }
        }
        updateVxInFluid(sprite, c, dT);
        sprite.setX(sprite.getExactX() + sprite.getVx() * dT);//  **更新x方向位置**
        //2.更新y方向上速度和位置
        if (land) {
            if (jump) {
                sprite.setVy(vJ);
            } else
                sprite.setVy(0);
        } else {
            sprite.setVy(sprite.getVy() + g * dT);
            updateVyInFluid(sprite, c, dT);
        }
        sprite.setY(sprite.getExactY() + sprite.getVy() * dT);//  **更新y方向位置**
        System.out.print("\r" + sprite);
    }

    @Override
    public void updateUncontrollable(RectSprite sprite) {
        double dT = this.dT;
        double c = this.c;
        boolean land = sprite.isLand();
        //1.更新x方向上速度和位置
        if (land) {
            double vx = sprite.getVx();
            double m = this.m;
            if (vx > m)
                sprite.setVx(sprite.getVx() - m);
            else if (vx < -m)
                sprite.setVx(sprite.getVx() + m);
            else
                sprite.setVx(0);
        }
        updateVxInFluid(sprite, c, dT);
        sprite.setX(sprite.getExactX() + sprite.getVx() * dT);//  **更新x方向位置**
        //2.更新y方向上速度和位置
        if (land) {
            sprite.setVy(0);
        } else {
            sprite.setVy(sprite.getVy() + g * dT);
            updateVyInFluid(sprite, c, dT);
        }
        sprite.setY(sprite.getExactY() + sprite.getVy() * dT);//  **更新y方向位置**
    }

    /**
     * 更新所有碰撞单位的参数。
     * <b>注意：RectUnit 单位必须在 RectSprite 的前方（索引位置）！并且可能会存在数据溢出的情况！</b>
     *
     * @param speed     防穿模临界相对速度，大于该速度才会触发x方向的防穿模机制
     * @param btdX      x方向上的防穿模距离
     * @param btdY      y方向上的防穿模距离
     * @param rectUnits 碰撞单位
     */
    public static void updateAllColliders(double speed, int btdX, int btdY, RectUnit... rectUnits) {
        int len = rectUnits.length;
        for (int i = 0; i < len; i++) {
            RectUnit self = rectUnits[i];
            if (self instanceof RectSprite s) {
                boolean collide = false;
                for (int j = i + 1; j < len; j++) {
                    RectUnit another = rectUnits[j];
                    if (another instanceof RectSprite a) {
                        RectSprite sCollider = s.getCollider();
                        if (sCollider == null) {
                            if (CollideTest.doubleRectUnit(s, a)) {
                                collide = true;
                                updateVxAndVyAfterCollide(s, a, s.getM(), a.getM(),
                                        s.getVx(), a.getVx(), s.getVy(), a.getVy());
                                s.setCollider(a);
                            }
                        } else if (CollideTest.doubleRectUnit(s, sCollider)) {//                   ** 精灵 和 精灵 碰撞 **
                            collide = true;
                            double sx = s.getExactX(), sCx = sCollider.getExactX();
                            double sy = s.getExactY(), sCy = sCollider.getExactY();
                            double tmp;
                            //1.x方向的处理
                            //相对速度
                            if (Math.abs(s.getVx() - sCollider.getVx()) > speed)
                                if (sx < sCx) {
                                    if ((tmp = s.getDiagonalX() - sCx) > 0 && tmp < btdX)
                                        while (s.getDiagonalX() > sCollider.getExactX()) {
                                            s.setX(s.getExactX() - 1);
                                            sCollider.setX(sCollider.getExactX() + 1);
                                        }
                                } else {
                                    if ((tmp = sCollider.getDiagonalX() - sx) > 0 && tmp < btdX)
                                        while (sCollider.getDiagonalX() > s.getExactX()) {
                                            s.setX(s.getExactX() + 1);
                                            sCollider.setX(sCollider.getExactX() - 1);
                                        }
                                }
                            //2.y方向的处理
                            if (sy < sCy) {
                                if ((tmp = s.getDiagonalY() - sCy) > 0 && tmp < btdY) {//s在sCollider的上方
                                    s.setY(sCy - s.getHeight());
                                    s.setLand(sCollider.isLand());//着陆标志位向上传递
                                }
                            } else {
                                if ((tmp = sCollider.getDiagonalY() - sy) > 0 && tmp < btdY) {//sCollider在s的上方
                                    sCollider.setY(sy - sCollider.getHeight());
                                    sCollider.setLand(s.isLand());//着陆标志位向上传递
                                }
                            }
                        } else
                            s.setCollider(null);
                    } else if (CollideTest.doubleRectUnit(s, another)) {//                       ** 精灵 和 固定物 碰撞 **
                        collide = true;
                        double tmp;
                        //x方向
                        if (another.getExactY() + btdY < s.getDiagonalY() &&
                                s.getExactY() < another.getDiagonalY() - btdY) {//碰撞发生在左右面
                            s.setVx(-s.getVx());//更新速度
                            //防穿模处理
                            if (s.getExactX() < another.getExactX()) {//s -> a
                                if ((tmp = s.getDiagonalX() - another.getExactX()) > 0)
                                    if (tmp < btdX) s.setX(another.getExactX() - s.getWidth());
                            } else {//a <- s
                                if ((tmp = another.getDiagonalX() - s.getExactX()) > 0)
                                    if (tmp < btdX) s.setX(another.getDiagonalX());
                            }
                        }
                        //y方向
                        if (another.getExactX() + btdX < s.getDiagonalX() &&
                                s.getExactX() < another.getDiagonalX() - btdX) {//碰撞发生在上下面
                            s.setVy(-s.getVy() / 2);//更新速度（能量损耗）// TODO: 2024/4/23 为什么弹跳会持续没完没了？
                            //防穿模处理
                            if (s.getExactY() < another.getExactY()) {//s -> a
                                if ((tmp = s.getDiagonalY() - another.getExactY()) > 0)
                                    if (tmp < btdY) {
                                        s.setY(another.getExactY() - s.getHeight());
                                        s.setLand(true);//着陆标志位的传递源！
                                        // TODO: 2024/4/23 设置着陆标志位后Vy速度会被设置为0，则不能在地上弹起如何解决？
                                    }
                            } else {//a <- s
                                if ((tmp = another.getDiagonalY() - s.getExactY()) > 0)
                                    if (tmp < btdY) s.setY(another.getDiagonalY());
                            }
                        }
                    }
                }
                if (!collide)
                    s.setLand(false);// TODO: 2024/4/23 如何设置两个在空中相互碰撞单位标志位？bug：两个碰撞单位悬空
            }
        }
    }

    /**
     * 更新物体 <i>弹性碰撞</i> 后的速度。<i>弹性碰撞：即不考虑能量损耗。</i>
     * <br><b>注意：可能会存在数据溢出的情况！</b></br>
     *
     * @param s1  精灵1
     * @param s2  精灵2
     * @param m1  精灵1的质量
     * @param m2  精灵2的质量
     * @param vx1 精灵1的x方向上速度
     * @param vx2 精灵2的x方向上速度
     * @param vy1 精灵1的y方向上速度
     * @param vy2 精灵2的y方向上速度
     */
    public static void updateVxAndVyAfterCollide(RectSprite s1, RectSprite s2, double m1, double m2,
                                                 double vx1, double vx2, double vy1, double vy2) {
        double mAdd = m1 + m2;
        double p = (m1 - m2) / mAdd, q1 = 2 * m1 / mAdd, q2 = 2 * m2 / mAdd;
        s1.setVxAndVy(p * vx1 + q2 * vx2, p * vy1 + q2 * vy2);
        s2.setVxAndVy(q1 * vx1 - p * vx2, q1 * vy1 - p * vy2);
    }

    /**
     * 更新物体 <i>在流体中</i> 的速度（x方向）。
     * <br><b>注意：可能会存在数据溢出的情况（几乎不会）！</b></br>
     *
     * @param sprite 受流体阻力影响的对象
     * @param c      阻力系数
     * @param dT     时间更新间隔
     */
    public static void updateVxInFluid(RectSprite sprite, double c, double dT) {
        double v = sprite.getVx();
        //阻力：f := c * sx * v^2 == m * a   =>   加速度：a == c * sx * v^2 / m; sx := sprite.height
        double a = c * v / sprite.getM() * v * sprite.getHeight();//可能数据会溢出
        if (v > 0)
            sprite.setVx(sprite.getVx() - a * dT);
        else
            sprite.setVx(sprite.getVx() + a * dT);
    }

    /**
     * 更新物体 <i>在流体中</i> 的速度（y方向）。
     * <br><b>注意：可能会存在数据溢出的情况（几乎不会）！</b></br>
     *
     * @param sprite 受流体阻力影响的对象
     * @param c      阻力系数
     * @param dT     时间更新间隔
     */
    public static void updateVyInFluid(RectSprite sprite, double c, double dT) {
        double v = sprite.getVy();
        //阻力：f := c * sy * v^2 == m * a   =>   加速度：a == c * sy * v^2 / m; sy := sprite.width
        double a = c * v / sprite.getM() * v * sprite.getWidth();//可能数据会溢出
        if (v > 0)
            sprite.setVy(sprite.getVy() - a * dT);
        else
            sprite.setVy(sprite.getVy() + a * dT);
    }

}
