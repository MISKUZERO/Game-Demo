package component;

public abstract class RectSprite extends RectUnit implements Sprite {

    private double m;//质量（kg）
    private double vx;//x方向速度（pixel/s）
    private double vy;//y方向速度（pixel/s）
    private boolean land;//是否着陆
    private RectSprite collider;//碰撞者

    public RectSprite() {
    }

    public RectSprite(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public RectSprite(double x, double y, int width, int height, double m) {
        super(x, y, width, height);
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

    public boolean isLand() {
        return land;
    }

    public void setLand(boolean land) {
        this.land = land;
    }

    public RectSprite getCollider() {
        return collider;
    }

    public void setCollider(RectSprite collider) {
        this.collider = collider;
    }

    public void setVxAndVy(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public String toString() {
        String hash;
        if (collider == null)
            hash = "null";
        else
            hash = collider.hashCode() + "";
        return "RectSprite{" +
                "vx=" + vx +
                ", vy=" + vy +
                ", collider=" + hash +
                ", land=" + land +
                "} " + super.toString();
    }
}
