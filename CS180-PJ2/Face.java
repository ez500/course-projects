public class Face {
    private Triangle[] mesh;
    private UnitVector surfaceNormal;

    public Face(Triangle one, Triangle two) {
        if (one.toString().equals("[InvalidTriangle]")
                || two.toString().equals("[InvalidTriangle]")
                || one.compareWith(two)) {
            this.mesh = new Triangle[] {new Triangle(), new Triangle()};
            this.surfaceNormal = new UnitVector();
            return;
        }

        int sharedV = 0;

        for (Point v1 : one.getVertices()) {
            for (Point v2 : two.getVertices()) {
                sharedV += v1.compareWith(v2) ? 1 : 0;
            }
        }

        if (sharedV < 2 || !one.getSurfaceNormal().compareWith(two.getSurfaceNormal())) {
            this.mesh = new Triangle[] {new Triangle(), new Triangle()};
            this.surfaceNormal = new UnitVector();
            return;
        }

        this.mesh = new Triangle[] {one, two};
        this.surfaceNormal = one.getSurfaceNormal();
    }

    public Face() {
        this(new Triangle(), new Triangle());
    }

    public Triangle[] getMesh() {
        return this.mesh;
    }

    public UnitVector getSurfaceNormal() {
        return this.surfaceNormal;
    }

    public boolean compareWith(Face face) {
        return this.mesh[0].compareWith(face.getMesh()[0])
            && this.mesh[1].compareWith(face.getMesh()[1])
            && this.surfaceNormal.compareWith(face.getSurfaceNormal());
    }

    public String toString() {
        if (this.mesh[0].toString().equals("[InvalidTriangle]")
                || this.mesh[1].toString().equals("[InvalidTriangle]")) {
            return "{InvalidFace}";
        }
        
        String oneStr = this.mesh[0].toString();
        String twoStr = this.mesh[1].toString();

        oneStr = oneStr.substring(0, oneStr.indexOf("; N")) + "]";
        twoStr = twoStr.substring(0, twoStr.indexOf("; N")) + "]";

        return String.format(
                "{F%s %s N%s}",
                oneStr,
                twoStr,
                this.surfaceNormal.toString()
        );
    }
}
