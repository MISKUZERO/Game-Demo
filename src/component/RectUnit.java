package component;

public abstract class RectUnit implements Unit {

    private int width;
    private int height;
    //左上角坐标
    private double x;
    private double y;

    public RectUnit() {
    }

    public RectUnit(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public RectUnit(int width, int height, double x, double y) {
        this(width, height);
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public final double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidthAndHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setXAndY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "RectUnit{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
