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
            if (jump)
                sprite.setVy(vJ);
            else
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
     * <b>注意：可能会存在数据溢出的情况！</b>
     *
     * @param speed     防穿模临界相对速度，大于该速度才会触发x方向的防穿模机制
     * @param btDx      x方向上的防穿模距离
     * @param btDy      y方向上的防穿模距离
     * @param rectUnits 碰撞单位
     */
    public static void updateAllColliders(double speed, double btDx, double btDy, RectUnit... rectUnits) {
        int len = rectUnits.length;
        for (int i = 0; i < len; i++) {
            RectUnit self = rectUnits[i];
            if (self instanceof RectSprite s) {
                for (int j = i + 1; j < len; j++) {
                    RectUnit another = rectUnits[j];
                    if (another instanceof RectSprite a) {//精灵和精灵碰撞检测
                        RectSprite sCollider = s.getCollider();
                        if (sCollider == null) {
                            if (CollideTest.isColliding(s, a)) {
                                updateVxAndVyAfterCollide(s, a, s.getM(), a.getM(),
                                        s.getVx(), a.getVx(), s.getVy(), a.getVy());
                                s.setCollider(a);
                                break;
                            }
                        } else if (CollideTest.isColliding(s, sCollider)) {//精灵和精灵相交，防止穿模和着陆判定
                            double sx = s.getExactX(), sCx = sCollider.getExactX();
                            double sy = s.getExactY(), sCy = sCollider.getExactY();
                            double tmp;
                            //1.x方向的处理
                            //相对速度
                            if (Math.abs(s.getVx() - sCollider.getVx()) > speed)
                                if (sx < sCx) {
                                    if ((tmp = s.getDiagonalX() - sCx) > 0 && tmp < btDx)
                                        while (s.getDiagonalX() > sCollider.getExactX()) {
                                            s.setX(s.getExactX() - 1);
                                            sCollider.setX(sCollider.getExactX() + 1);
                                        }
                                } else {
                                    if ((tmp = sCollider.getDiagonalX() - sx) > 0 && tmp < btDx)
                                        while (sCollider.getDiagonalX() > s.getExactX()) {
                                            s.setX(s.getExactX() + 1);
                                            sCollider.setX(sCollider.getExactX() - 1);
                                        }
                                }
                            //2.y方向的处理
                            if (sy < sCy) {
                                if (s.getDiagonalY() - sCy < btDy) {//s在sCollider的上方
                                    s.setY(sCy - s.getHeight());
                                    s.setVy(sCollider.getVy());
                                    s.setLand(sCollider.isLand());//着陆标志位向上传递
                                }
                            } else {
                                if (sCollider.getDiagonalY() - sy < btDy) {//sCollider在s的上方
                                    sCollider.setY(sy - sCollider.getHeight());
                                    sCollider.setVy(s.getVy());
                                    sCollider.setLand(s.isLand());//着陆标志位向上传递
                                }
                            }
//                            updateVxAndVyAfterCollide(s, sCollider, s.getM(), sCollider.getM(),
//                                    s.getVx(), sCollider.getVx(), s.getVy(), sCollider.getVy());
                            break;
                        } else
                            s.setCollider(null);
                    } else if (CollideTest.isColliding(s, another)) {//精灵和固定物碰撞检测
                        System.err.println("精灵和固定物碰撞检测！");
                        //1.固定物的上表面碰撞处理
                        //2.固定物的下表面碰撞处理
                        //3.固定物的左表面碰撞处理
                        //4.固定物的右表面碰撞处理
                    }

                }
            } else
                for (int j = i + 1; j < len; j++)
                    if (rectUnits[j] instanceof RectSprite a) {
                        if (CollideTest.isColliding(self, a)) {//固定物和精灵碰撞检测
                            //1.固定物的上表面碰撞处理
                            a.setVy(-a.getVy() / 2);//与固定物上表面碰撞
                            //2.固定物的下表面碰撞处理
                            //3.固定物的左表面碰撞处理
                            //4.固定物的右表面碰撞处理
                            //后处理
                            double sy = self.getExactY();
                            int ay = a.getDiagonalY();
                            if (ay > sy) {
                                a.setY(sy - a.getHeight());//防止陷入固体物上表面
                                a.setLand(true);//  **着陆标志位的传递源**
                            }
                        } else
                            a.setLand(false);
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
