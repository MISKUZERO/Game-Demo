package util;

import component.RectUnit;

public class CollideTest {

    @Deprecated
    public static boolean _doubleRect(int x1, int y1, int x2, int y2,
                                      int x3, int y3, int x4, int y4) {
        return Math.abs(x1 - x3) < Math.min(x2 - x1, x4 - x3)
                && Math.abs(y1 - y3) < Math.min(y2 - y1, y4 - y3);
    }

    public static boolean doubleRect(int x1, int y1, int x2, int y2,
                                     int x3, int y3, int x4, int y4) {
        return x1 <= x4 && x2 >= x3 && y1 <= y4 && y2 >= y3;
    }

    public static boolean doubleRectUnit(RectUnit r1, RectUnit r2) {
        return doubleRect(r1.getX(), r1.getY(), r1.getDiagonalX(), r1.getDiagonalY(),
                r2.getX(), r2.getY(), r2.getDiagonalX(), r2.getDiagonalY());
    }

    public static boolean lineAndRect(int x1, int y1, int x2, int y2,
                                      int x3, int y3, int x4, int y4) {
        double x, y, k = ((double) y2 - y1) / (x2 - x1);
        /*
         *   直线方程：
         *       y = k * (x - x1) + y1;
         *       x = (y - y1) / k + x1;
         *       范围：x: [xl, xr], y: [yl, yr];
         *       xl = min(x1, x2), xr = max(x1, x2), yl = min(y1, y2), yr = max(y1, y2);
         */
        int xl, xr, yl, yr;
        if (x1 < x2) {
            xl = x1;
            xr = x2;
        } else {
            xl = x2;
            xr = x1;
        }
        if (y1 < y2) {
            yl = y1;
            yr = y2;
        } else {
            yl = y2;
            yr = y1;
        }
        //1.左边检验：x = x3
        if (xl <= x3 && x3 <= xr) {
            y = k * (x3 - x1) + y1;
            if (y3 <= y && y <= y4) return true;
        }
        //2.右边检验: x = x4
        if (xl <= x4 && x4 <= xr) {
            y = k * (x4 - x1) + y1;
            if (y3 <= y && y <= y4) return true;
        }
        //3.上边检验：y = y3
        if (yl <= y3 && y3 <= yr) {
            x = (y3 - y1) / k + x1;
            if (x3 <= x && x <= x4) return true;
        }
        //4.下边检验：y = y4
        if (yl <= y4 && y4 <= yr) {
            x = (y4 - y1) / k + x1;
            return x3 <= x && x <= x4;
        }
        return false;
    }

}
