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
    public void updateRole(RectActor actor, boolean lMove, boolean rMove, boolean jump) {
        int ground = 600 - 68;//地面高度
        //确定横向参数
        if (lMove && !rMove) {//仅按A键
            if (actor.getY() == ground) { //在地上
                actor.setVx(actor.getVx() - a * dT);
                if (actor.getVx() < -vRMax)
                    actor.setVx(-vRMax);
            }
            double xl = actor.getXl() + actor.getVx() * dT;
            actor.setXl(xl);
            actor.setXr(xl);
            actor.setXm(xl);
            actor.setX((int) xl);
        } else if (rMove && !lMove) {//仅按D键
            if (actor.getY() == ground) {//在地上
                actor.setVx(actor.getVx() + a * dT);
                if (actor.getVx() > vRMax)
                    actor.setVx(vRMax);
            }
            double xr = actor.getXr() + actor.getVx() * dT;
            actor.setXl(xr);
            actor.setXr(xr);
            actor.setXm(xr);
            actor.setX((int) xr);
        } else {
            double vx = actor.getVx();
            if (vx > m)
                actor.setVx(actor.getVx() - m);
            else if (vx < -m)
                actor.setVx(actor.getVx() + m);
            else
                actor.setVx(0);
            double xm = actor.getXm() + actor.getVx() * dT;
            actor.setXl(xm);
            actor.setXr(xm);
            actor.setXm(xm);
            actor.setX((int) xm);
        }
        //确定纵向参数
        if (actor.getY() == ground) {//在地上
            if (jump)
                actor.setVy(vJ);
        } else
            actor.setVy(actor.getVy() + g * dT);
        actor.setYh(actor.getYh() + actor.getVy() * dT);
        if (actor.getYh() > ground)
            actor.setYh(ground);
        actor.setY((int) actor.getYh());
    }

    @Override
    public void updateLifeless(RectActor actor) {
        int ground = 600 - 68;//地面高度
        //确定横向参数
//        if (false) {
//            if (actor.getY() == ground) { //在地上
//                actor.setVx(actor.getVx() - a * dT);
//                if (actor.getVx() < -vRMax)
//                    actor.setVx(-vRMax);
//            }
//            double xl = actor.getXl() + actor.getVx() * dT;
//            actor.setXl(xl);
//            actor.setXr(xl);
//            actor.setXm(xl);
//            actor.setX((int) xl);
//        }
        //确定纵向参数
        if (actor.getY() != ground) //在空中
            actor.setVy(actor.getVy() + g * dT);
        actor.setYh(actor.getYh() + actor.getVy() * dT);
        if (actor.getYh() > ground)
            actor.setYh(ground);
        actor.setY((int) actor.getYh());
    }

}
