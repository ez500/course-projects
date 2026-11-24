package io.github.ez500.CS180_GPJ.util;

/**
 *
 * Represents an ordered pair on the 2D Cartesian plane.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public class Coordinate implements Pair<Double, Double> {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return getFirst();
    }

    @Override
    public Double getFirst() {
        return this.x;
    }

    public double y() {
        return getSecond();
    }

    @Override
    public Double getSecond() {
        return this.y;
    }

    public void setX(double x) {
        setFirst(x);
    }

    @Override
    public void setFirst(Double x) {
        this.x = x;
    }

    public void setY(double y) {
        setSecond(y);
    }

    @Override
    public void setSecond(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
