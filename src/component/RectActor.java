package component;

public abstract class RectActor extends RectUnit implements Sprite {

    double vx;//x方向速度（pixel/s）
    double vy;//y方向速度（pixel/s）

    public RectActor() {
    }

    public RectActor(int width, int height, double x, double y) {
        super(width, height, x, y);
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
                "vx=" + vx +
                ", vy=" + vy +
                "} " + super.toString();
    }
}
