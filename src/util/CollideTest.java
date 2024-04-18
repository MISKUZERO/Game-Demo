package util;

import component.RectUnit;

public class CollideTest {

    @Deprecated
    public static boolean isColliding(int x1, int y1, int x2, int y2,
                                      int x3, int y3, int x4, int y4) {
        return Math.abs(x1 - x3) < Math.min(x2 - x1, x4 - x3)
                && Math.abs(y1 - y3) < Math.min(y2 - y1, y4 - y3);
    }

    public static boolean _isColliding(int x1, int y1, int x2, int y2,
                                       int x3, int y3, int x4, int y4) {
        return x1 <= x4 && x2 >= x3 && y1 <= y4 && y2 >= y3;
    }

    public static boolean isColliding(RectUnit a1, RectUnit a2) {
        int a1X = a1.getX();
        int a1Y = a1.getY();
        int a2X = a2.getX();
        int a2Y = a2.getY();
        return _isColliding(a1X, a1Y, a1X + a1.getWidth(), a1Y + a1.getHeight(),
                a2X, a2Y, a2X + a2.getWidth(), a2Y + a2.getHeight());
    }
}
