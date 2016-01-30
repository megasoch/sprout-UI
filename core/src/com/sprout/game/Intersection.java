package com.sprout.game;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by megasoch on 19.12.2015.
 */
public class Intersection {

    private static int turn(PointElement a, PointElement b, PointElement c)
    {
        double res = (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
        double check = 0.1;

        if(res > check) {
            return 1;
        }

        if(res < -check) {
            return -1;
        }

        return 0;
    }

    private static boolean intersectionSegments(PointElement a, PointElement b, PointElement c, PointElement d)
    {
        if(turn(a, b, c) * turn(a, b, d) < 0)
        {
            if(turn(c, d, a) * turn(c, d, b) < 0)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean hasIntersection(PointElement p, Set<EdgeElement> edges, ArrayList<PointElement> edgePointSequence) {
        for (EdgeElement e: edges) {
            ArrayList<PointElement> curr = e.getPoints();
            for (int i = 0; i < curr.size() - 1; i++) {
                if (intersectionSegments(p, edgePointSequence.get(edgePointSequence.size() - 1), curr.get(i), curr.get(i + 1))) {
                    return true;
                }
            }
        }
        for (int i = 0; i < edgePointSequence.size() - 1; i++) {
            if (intersectionSegments(p, edgePointSequence.get(edgePointSequence.size() - 1), edgePointSequence.get(i), edgePointSequence.get(i + 1))) {
                return true;
            }
        }
        return false;
    }

    public static float length(PointElement a, PointElement b) {
        float sqrX = (a.getX() - b.getX()) * (a.getX() - b.getX());
        float sqrY = (a.getY() - b.getY()) * (a.getY() - b.getY());
        return (float)Math.sqrt(sqrX + sqrY);
    }

    public static float sequenceLength(ArrayList<PointElement> edgePointSequence) {
        float length = 0;
        for (int i = 0; i < edgePointSequence.size() - 1; i++) {
            PointElement a = edgePointSequence.get(i);
            PointElement b = edgePointSequence.get(i + 1);
            length += length(a, b);
        }
        return length;
    }

    private static boolean boundingBox(PointElement a, PointElement b, PointElement c, PointElement d)
    {
        return (a.getX() == c.getX() && a.getY() == c.getY() && b.getX() == d.getX() && b.getY() == d.getY());
    }
}
