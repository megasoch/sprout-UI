package com.sprout.game;

import java.io.Serializable;

/**
 * Created by megasoch on 04.12.2015.
 */
public class PointElement implements Serializable {
    private float x;
    private float y;
    private boolean selected;

    @Override
    public String toString() {
        return "PointElement{" +
                "x=" + x +
                ", y=" + y +
                ", selected=" + selected +
                ", power=" + power +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointElement that = (PointElement) o;

        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (selected != that.selected) return false;
        return power == that.power;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (selected ? 1 : 0);
        result = 31 * result + power;
        return result;
    }

    private int power;

    public PointElement(float x, float y, boolean selected) {
        this.x = x;
        this.y = y;
        this.selected = selected;
        power = 0;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {

        return power;
    }

    public PointElement(float x, float y) {
        this.x = x;
        this.y = y;
        power = 0;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {

        this.selected = selected;
    }

    public void increasePower() {
        power++;
    }

    public void decreasePower() {
        power--;
    }

    public boolean isUsable() {
        return power < 3 ? true : false;
    }
}
