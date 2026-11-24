public class Point {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point() {
        this(0.0, 0.0, 0.0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean compareWith(Point point) {
        return Math.abs(point.getX() - this.x) <= 0.0001
            && Math.abs(point.getY() - this.y) <= 0.0001
            && Math.abs(point.getZ() - this.z) <= 0.0001;
    }

    public String toString() {
        return String.format("(x%.3f, y%.3f, z%.3f)", this.x, this.y, this.z);
    }
}
