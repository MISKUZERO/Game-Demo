package controller;

import component.RectActor;

public class Reality implements ArgsController {

    private double dT;//时间间隔（s）
    private double m;//摩擦系数
    private double c;//空气阻力系数
    private double g;//重力加速度（pixel/s^2）
    private double a;//（右）奔跑加速度（pixel/s^2）
    private double vRMax;//奔跑最大速率（pixel/s）
    private double vJ;//跳跃速度（pixel/s）

    public Reality() {
        this(0.001, 0.2, 0.001, 10000, 2000, 500, -2000);
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
    public void updateControllable(RectActor actor, boolean... signals) {
        boolean lMove = signals[0], rMove = signals[1], jump = signals[2];//控制信号
        double dT = this.dT;
        double c = this.c;
        double y = actor.getY();
        int ground = 600 - 68;//地面高度
        boolean onGround = y == ground;//在地上
        //1.确定横向参数
        if (onGround) {
            if (lMove && !rMove) {//左移动
                actor.setVx(actor.getVx() - a * dT);
                double vRMax = -this.vRMax;
                if (actor.getVx() < vRMax)
                    actor.setVx(vRMax);
            } else if (rMove && !lMove) {//右移动
                actor.setVx(actor.getVx() + a * dT);
                double vRMax = this.vRMax;
                if (actor.getVx() > vRMax)
                    actor.setVx(vRMax);
            } else {
                double vx = actor.getVx();
                double m = this.m;
                if (vx > m)
                    actor.setVx(actor.getVx() - m);
                else if (vx < -m)
                    actor.setVx(actor.getVx() + m);
                else
                    actor.setVx(0);
            }
        }
        updateVxInFluid(actor, c, dT);
        actor.setX(actor.getX() + actor.getVx() * dT);//  **更新x方向位置**
        //2.确定纵向参数
        if (onGround) {
            if (jump)
                actor.setVy(vJ);
            else
                actor.setVy(0);
        } else {
            actor.setVy(actor.getVy() + g * dT);
            updateVyInFluid(actor, c, dT);
        }
        actor.setY(y + actor.getVy() * dT);//             **更新y方向位置**
        if (actor.getY() > ground)//保证不会陷入地下
            actor.setY(ground);
        System.out.print("\r" + actor);
    }

    @Override
    public void updateUncontrollable(RectActor actor) {
        double dT = this.dT;
        double c = this.c;
        double y = actor.getY();
        int ground = 600 - 68;//地面高度
        boolean onGround = y == ground;//在地上
        //1.确定横向参数
        if (onGround) {
            double vx = actor.getVx();
            double m = this.m;
            if (vx > m)
                actor.setVx(actor.getVx() - m);
            else if (vx < -m)
                actor.setVx(actor.getVx() + m);
            else
                actor.setVx(0);
        }
        updateVxInFluid(actor, c, dT);
        actor.setX(actor.getX() + actor.getVx() * dT);//  **更新x方向位置**
        //2.确定纵向参数
        if (onGround) {
            actor.setVy(0);
        } else {
            actor.setVy(actor.getVy() + g * dT);
            updateVyInFluid(actor, c, dT);
        }
        actor.setY(y + actor.getVy() * dT);//             **更新y方向位置**
        if (actor.getY() > ground)//保证不会陷入地下
            actor.setY(ground);
    }

    /**
     * 更新物体在流体中的速度（x方向）。
     * <b>注意：可能会存在数据溢出的情况（几乎不会）！</b>
     *
     * @param actor 受流体阻力影响的对象
     * @param c     阻力系数
     * @param dT    时间更新间隔
     */
    public static void updateVxInFluid(RectActor actor, double c, double dT) {
        double v = actor.getVx();
        //阻力：f := c * sx * v^2 == m * a   =>   加速度：a == c * sx * v^2 / m; sx := actor.height
        double a = c * v / actor.getM() * v * actor.getHeight();//可能数据会溢出
        if (v > 0)
            actor.setVx(actor.getVx() - a * dT);
        else
            actor.setVx(actor.getVx() + a * dT);
    }

    /**
     * 更新物体在流体中的速度（y方向）。
     * <b>注意：可能会存在数据溢出的情况（几乎不会）！</b>
     *
     * @param actor 受流体阻力影响的对象
     * @param c     阻力系数
     * @param dT    时间更新间隔
     */
    public static void updateVyInFluid(RectActor actor, double c, double dT) {
        double v = actor.getVy();
        //阻力：f := c * sy * v^2 == m * a   =>   加速度：a == c * sy * v^2 / m; sy := actor.width
        double a = c * v / actor.getM() * v * actor.getWidth();//可能数据会溢出
        if (v > 0)
            actor.setVy(actor.getVy() - a * dT);
        else
            actor.setVy(actor.getVy() + a * dT);
    }

}
