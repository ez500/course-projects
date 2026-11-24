public class UnitVector {
    private double i;
    private double j;
    private double k;

    public UnitVector(double i, double j, double k) {
        double mag = Math.sqrt(i * i + j * j + k * k);

        if (Math.abs(mag) <= 0.0001) {
            this.i = 0.0;
            this.j = 0.0;
            this.k = 0.0;
        } else if (Math.abs(mag - 1) <= 0.0001) {
            this.i = i;
            this.j = j;
            this.k = k;
        } else {
            this.i = i / mag;
            this.j = j / mag;
            this.k = k / mag;
        }
    }

    public UnitVector(Point start, Point end) {
        this(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }

    public UnitVector() {
        this(0.0, 0.0, 0.0);
    }

    public double getI() {
        return this.i;
    }

    public double getJ() {
        return this.j;
    }

    public double getK() {
        return this.k;
    }

    public boolean compareWith(UnitVector vector) {
        return Math.abs(vector.getI() - this.i) <= 0.0001
            && Math.abs(vector.getJ() - this.j) <= 0.0001
            && Math.abs(vector.getK() - this.k) <= 0.0001;
    }

    public UnitVector crossProduct(UnitVector b) {
        return new UnitVector(
                this.j * b.getK() - this.k * b.getJ(),
                this.k * b.getI() - this.i * b.getK(),
                this.i * b.getJ() - this.j * b.getI()
        );
    }

    public String toString() {
        if (Math.abs(this.i) <= 0.0001
                && Math.abs(this.j) <= 0.0001
                && Math.abs(this.k) <= 0.0001) {
            return "<InvalidUnitVector>";
        }
        return String.format("<%.3fi, %.3fj, %.3fk>", this.i, this.j, this.k);
    }
}
