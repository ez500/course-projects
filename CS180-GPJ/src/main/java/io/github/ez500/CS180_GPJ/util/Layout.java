package io.github.ez500.CS180_GPJ.util;

import java.util.Arrays;

/**
 *
 * Represents a layout of a reservable (e.g., table) as mapped onto a chart.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public class Layout implements Pair<Coordinate, Layout.Direction> {

    public interface Vectorizable {
        Coordinate getVector();
    }

    public enum Direction implements Vectorizable {
        NORTH(new Coordinate(0.0, 1.0)),
        SOUTH(new Coordinate(0.0, -1.0)),
        EAST(new Coordinate(1.0, 0.0)),
        WEST(new Coordinate(-1.0, 0.0));

        private final Coordinate cardinalVector;

        Direction(Coordinate cardinalVector) {
            this.cardinalVector = cardinalVector;
        }

        @Override
        public Coordinate getVector() {
            return this.cardinalVector;
        }
    }

    private Coordinate origin;
    private Direction direction;

    public Layout(Coordinate origin, Direction direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Layout(double x, double y, Direction direction) {
        this.origin = new Coordinate(x, y);
        this.direction = direction;
    }

    public Layout(double x, double y, String direction) {
        this.origin = new Coordinate(x, y);
        this.direction = Direction.valueOf(direction);
    }

    public static Layout of(String data) {
        // Format: "((x,y),DIRECTION)"
        String[] parts = data.split(",");
        double x = Double.parseDouble(parts[0].substring(2));
        double y = Double.parseDouble(parts[1].substring(0, parts[1].length() - 1));
        Direction direction = Direction.valueOf(parts[2].substring(0, parts[2].length() - 1));
        return new Layout(x, y, direction);
    }

    public Coordinate getOrigin() {
        return getFirst();
    }

    @Override
    public Coordinate getFirst() {
        return this.origin;
    }

    public Direction getDirection() {
        return getSecond();
    }

    @Override
    public Direction getSecond() {
        return this.direction;
    }

    public void setOrigin(Coordinate origin) {
        setFirst(origin);
    }

    public void setOrigin(double x, double y) {
        setOrigin(new Coordinate(x, y));
    }

    @Override
    public void setFirst(Coordinate origin) {
        this.origin = origin;
    }

    public void setDirection(Direction direction) {
        setSecond(direction);
    }

    @Override
    public void setSecond(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format(
            "(%s,%s)",
            this.origin.toString(),
            this.direction.name()
        );
    }
}
