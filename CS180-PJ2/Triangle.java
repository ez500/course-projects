public class Triangle {
    private Point vertexA;
    private Point vertexB;
    private Point vertexC;
    private UnitVector surfaceNormal;

    public Triangle(Point vertA, Point vertB, Point vertC) {
        this.vertexA = vertA;
        this.vertexB = vertB;
        this.vertexC = vertC;
        this.surfaceNormal =
            new UnitVector(vertA, vertB).crossProduct(new UnitVector(vertA, vertC));
    }

    public Triangle() {
        this(new Point(), new Point(), new Point());
    }

    public Point getVertexA() {
        return this.vertexA;
    }

    public Point getVertexB() {
        return this.vertexB;
    }

    public Point getVertexC() {
        return this.vertexC;
    }

    public UnitVector getSurfaceNormal() {
        return this.surfaceNormal;
    }

    public Point[] getVertices() {
        return new Point[] {this.vertexA, this.vertexB, this.vertexC};
    }

    public boolean compareWith(Triangle triangle) {
        return this.vertexA.compareWith(triangle.getVertexA())
            && this.vertexB.compareWith(triangle.getVertexB())
            && this.vertexC.compareWith(triangle.getVertexC())
            && this.surfaceNormal.compareWith(triangle.getSurfaceNormal());
    }

    public String toString() {
        if (vertexA.compareWith(vertexB)
                || vertexA.compareWith(vertexC)
                || vertexB.compareWith(vertexC)
                || surfaceNormal.toString().equals("<InvalidUnitVector>")) {
            return "[InvalidTriangle]";
        }
        return String.format(
                "[A%s; B%s; C%s; N%s]",
                this.vertexA.toString(),
                this.vertexB.toString(),
                this.vertexC.toString(),
                this.surfaceNormal.toString()
        );
    }
}
