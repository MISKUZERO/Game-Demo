package component;

public abstract class RectUnit implements Unit {
    //左上角坐标
    private double x;
    private double y;
    private int width;
    private int height;

    public RectUnit() {
    }

    public RectUnit(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public RectUnit(double x, double y, int width, int height) {
        this(width, height);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getDiagonalX() {
        return (int) (x + width);
    }

    public int getDiagonalY() {
        return (int) (y + height);
    }

    public double getExactX() {
        return x;
    }

    public final double getExactY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
