package component;

public abstract class RectActor extends RectUnit implements Sprite {

    double xl;//左位移记录（pixel）
    double xr;//右位移记录（pixel）
    double xm;//保持位移记录（pixel）
    double yh;//高度位移记录（pixel）

    double vx;//x方向速度（pixel/s）
    double vy;//y方向速度（pixel/s）

    public RectActor() {
    }

    public RectActor(int width, int height, int x, int y) {
        super(width, height, x, y);
        xl = xr = xm = x;
        yh = y;
    }

    public double getXl() {
        return xl;
    }

    public void setXl(double xl) {
        this.xl = xl;
    }

    public double getXr() {
        return xr;
    }

    public void setXr(double xr) {
        this.xr = xr;
    }

    public double getXm() {
        return xm;
    }

    public void setXm(double xm) {
        this.xm = xm;
    }

    public double getYh() {
        return yh;
    }

    public void setYh(double yh) {
        this.yh = yh;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setArgs(double xl, double xr, double xm, double yh, double vx, double vy) {
        this.xl = xl;
        this.xr = xr;
        this.xm = xm;
        this.yh = yh;
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public String toString() {
        return "RectActor{" +
                "vx=" + vx +
                ", vy=" + vy +
                "} " + super.toString();
    }
}
