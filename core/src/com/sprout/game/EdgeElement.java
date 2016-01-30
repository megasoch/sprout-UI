package com.sprout.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by megasoch on 04.12.2015.
 */
public class EdgeElement implements Serializable {
    private PointElement from;

    @Override
    public String toString() {
        return "EdgeElement{" +
                "from=" + from +
                ", to=" + to +
                ", points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeElement that = (EdgeElement) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return !(points != null ? !points.equals(that.points) : that.points != null);

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }

    private PointElement to;

    ArrayList<PointElement> points = new ArrayList<PointElement>(0);

    public ArrayList<PointElement> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PointElement> points) {
        this.points = points;
    }

    public EdgeElement(ArrayList<PointElement> points) {
        this.points = points;
    }

    public PointElement getFrom() {
        return from;
    }

    public PointElement getTo() {
        return to;
    }
}
