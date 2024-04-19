package component;

public abstract class RectActor extends RectUnit implements Sprite {

    double m;//质量（kg）
    double vx;//x方向速度（pixel/s）
    double vy;//y方向速度（pixel/s）

    public RectActor() {
    }

    public RectActor(int width, int height, double x, double y) {
        super(width, height, x, y);
    }

    public RectActor(int width, int height, double x, double y, double m) {
        super(width, height, x, y);
        this.m = m;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
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

    @Override
    public String toString() {
        return "RectActor{" +
                "m=" + m +
                ", vx=" + vx +
                ", vy=" + vy +
                "} " + super.toString();
    }
}
