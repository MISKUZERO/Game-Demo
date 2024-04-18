package controller;

import component.RectActor;

public class Reality implements ArgsController {

    private double friction;//摩擦因子
    private double deltaT;//时间间隔（s）
    private double gravity;//重力加速度（pixel/s^2）
    private double acceleration;//（右）奔跑加速度（pixel/s^2）
    private double vRunMax;//最大奔跑速率（pixel/s）
    private double vJump;//跳跃速度（pixel/s）

    public Reality() {
        this(0.001, 0.2, 10000, 2000, 500, -2000);
    }

    public Reality(double acceleration, double vRunMax, double vJump) {
        this(0.001, 0.2, 10000, acceleration, vRunMax, vJump);
    }

    public Reality(double deltaT, double friction, double gravity, double acceleration, double vRunMax, double vJump) {
        this.acceleration = acceleration;
        this.vRunMax = vRunMax;
        this.vJump = vJump;
        this.deltaT = deltaT;
        this.friction = friction;
        this.gravity = gravity;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getVRunMax() {
        return vRunMax;
    }

    public void setVRunMax(double vRunMax) {
        this.vRunMax = vRunMax;
    }

    public double getVJump() {
        return vJump;
    }

    public void setVJump(double vJump) {
        this.vJump = vJump;
    }

    public void updateRole(RectActor actor, boolean lMove, boolean rMove, boolean jump) {
        int ground = 600 - 68;//地面高度
        //确定横向参数
        if (lMove && !rMove) {//仅按A键
            if (actor.getY() == ground) { //在地上
                actor.setVx(actor.getVx() - acceleration * deltaT);
                if (actor.getVx() < -vRunMax)
                    actor.setVx(-vRunMax);
            }
            double xl = actor.getXl() + actor.getVx() * deltaT;
            actor.setXl(xl);
            actor.setXr(xl);
            actor.setXm(xl);
            actor.setX((int) xl);
        } else if (rMove && !lMove) {//仅按D键
            if (actor.getY() == ground) {//在地上
                actor.setVx(actor.getVx() + acceleration * deltaT);
                if (actor.getVx() > vRunMax)
                    actor.setVx(vRunMax);
            }
            double xr = actor.getXr() + actor.getVx() * deltaT;
            actor.setXl(xr);
            actor.setXr(xr);
            actor.setXm(xr);
            actor.setX((int) xr);
        } else {
            double vx = actor.getVx();
            if (vx > friction)
                actor.setVx(actor.getVx() - friction);
            else if (vx < -friction)
                actor.setVx(actor.getVx() + friction);
            else
                actor.setVx(0);
            double xm = actor.getXm() + actor.getVx() * deltaT;
            actor.setXl(xm);
            actor.setXr(xm);
            actor.setXm(xm);
            actor.setX((int) xm);
        }
        //确定纵向参数
        if (actor.getY() == ground) {//在地上
            if (jump)
                actor.setVy(vJump);
        } else
            actor.setVy(actor.getVy() + gravity * deltaT);
        actor.setYh(actor.getYh() + actor.getVy() * deltaT);
        if (actor.getYh() > ground)
            actor.setYh(ground);
        actor.setY((int) actor.getYh());
    }

    @Override
    public void updateLifeless(RectActor actor) {

    }

}
