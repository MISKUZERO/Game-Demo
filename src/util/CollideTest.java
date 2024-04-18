package util;

import component.RectActor;

public class CollideTest {

    public static boolean isCollide(int x1, int y1, int x2, int y2,
                                    int x3, int y3, int x4, int y4) {
        return x1 <= x4 && x3 <= x2 && y2 <= y3 && y4 <= y1;
    }


    public static boolean isCollide(RectActor a1, RectActor a2) {
        int a1X = a1.getX();
        int a1Y = a1.getY();
        int a2X = a2.getX();
        int a2Y = a2.getY();
        return isCollide(a1X, a1Y, a1X + a1.getWidth(), a1Y + a1.getHeight(),
                a2X, a2Y, a2X + a2.getWidth(), a2Y + a2.getHeight());
    }
}
