package component;

public abstract class RectUnit implements Unit {

    private int width;
    private int height;
    //左上角坐标
    private int x;
    private int y;

    public RectUnit() {
    }

    public RectUnit(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public RectUnit(int width, int height, int x, int y) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public final int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidthAndHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setXAndY(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
