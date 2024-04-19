package controller;

import component.RectActor;

public class Reality implements ArgsController {

    private double dT;//时间间隔（s）
    private double m;//摩擦系数
    private double c;//空气阻力系数 空气阻力：f = c * v^2
    private double g;//重力加速度（pixel/s^2）
    private double a;//（右）奔跑加速度（pixel/s^2）
    private double vRMax;//奔跑最大速率（pixel/s）
    private double vJ;//跳跃速度（pixel/s）

    public Reality() {
        this(0.001, 0.2, 0.5, 10000, 2000, 500, -2000);
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
        double y = actor.getY();
        int ground = 600 - 68;//地面高度
        boolean onGround = y == ground;//在地上
        //1.确定横向参数
        if (onGround) {
            if (lMove && !rMove) {//仅按A键
                actor.setVx(actor.getVx() - a * dT);
                if (actor.getVx() < -vRMax)
                    actor.setVx(-vRMax);
            } else if (rMove && !lMove) {//仅按D键
                actor.setVx(actor.getVx() + a * dT);
                if (actor.getVx() > vRMax)
                    actor.setVx(vRMax);
            } else {
                double vx = actor.getVx();
                if (vx > m)
                    actor.setVx(actor.getVx() - m);
                else if (vx < -m)
                    actor.setVx(actor.getVx() + m);
                else
                    actor.setVx(0);
            }
        }
        actor.setX(actor.getX() + actor.getVx() * dT);//  **更新x方向位置**
        //2.确定纵向参数
        if (onGround) {
            if (jump)
                actor.setVy(vJ);
        } else
            actor.setVy(actor.getVy() + g * dT);
        actor.setY(y + actor.getVy() * dT);//             **更新y方向位置**
        if (actor.getY() > ground)//保证不会陷入地下
            actor.setY(ground);
    }

    @Override
    public void updateUncontrollable(RectActor actor) {
        double y = actor.getY();
        int ground = 600 - 68;//地面高度
        boolean onGround = y == ground;//在地上
        // TODO: 2024/4/19   1.确定横向参数
        //2.确定纵向参数
        if (!onGround) //在空中
            actor.setVy(actor.getVy() + g * dT);
        actor.setY(y + actor.getVy() * dT);//             **更新y方向位置**
        if (actor.getY() > ground)//保证不会陷入地下
            actor.setY(ground);
    }

}
