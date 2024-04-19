package util;

import component.RectUnit;

public class CollideTest {

    @Deprecated
    public static boolean _isColliding(int x1, int y1, int x2, int y2,
                                       int x3, int y3, int x4, int y4) {
        return Math.abs(x1 - x3) < Math.min(x2 - x1, x4 - x3)
                && Math.abs(y1 - y3) < Math.min(y2 - y1, y4 - y3);
    }

    public static boolean isColliding(int x1, int y1, int x2, int y2,
                                      int x3, int y3, int x4, int y4) {
        return x1 <= x4 && x2 >= x3 && y1 <= y4 && y2 >= y3;
    }

    public static boolean isColliding(RectUnit r1, RectUnit r2) {
        return isColliding(r1.getX(), r1.getY(), r1.getDiagonalX(), r1.getDiagonalY(),
                r2.getX(), r2.getY(), r2.getDiagonalX(), r2.getDiagonalY());
    }

}
