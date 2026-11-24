public class Cube {
    private Face[] mesh;

    public Cube(Face one, Face two, Face three, Face four, Face five, Face six) {
        Face[] faces = {one, two, three, four, five, six};
        Face[] invalid = 
            {new Face(), new Face(), new Face(), new Face(), new Face(), new Face()};

        if (one.toString().equals("{InvalidFace}")
                || two.toString().equals("{InvalidFace}")
                || three.toString().equals("{InvalidFace}")
                || four.toString().equals("{InvalidFace}")
                || five.toString().equals("{InvalidFace}")
                || six.toString().equals("{InvalidFace}")) {
            this.mesh = invalid;
            return;
        }

        for (int i = 0; i < faces.length; i++) {
            for (int j = i + 1; j < faces.length; j++) {
                if (faces[i].compareWith(faces[j])) {
                    this.mesh = invalid;
                    return;
                }
            }
        }

        for (int i = 0; i < faces.length; i++) {
            int validEdges = 0;
            for (int j = 0; j < faces.length; j++) {
                if (i == j) continue;

                Face a = faces[i];
                Face b = faces[j];
                Point[] aPts = new Point[4];
                int aSz = 0;
                Point[] bPts = new Point[4];
                int bSz = 0;
                int sharedV = 0;

                for (Triangle t : a.getMesh()) {
                    aPoints:
                    for (Point pt : t.getVertices()) {
                        for (int k = 0; k < aSz; k++) {
                            if (pt.compareWith(aPts[k])) {
                                continue aPoints;
                            }
                        }
                        aPts[aSz] = pt;
                        aSz++;
                    }
                }

                for (Triangle t : b.getMesh()) {
                    bPoints:
                    for (Point pt : t.getVertices()) {
                        for (int k = 0; k < bSz; k++) {
                            if (pt.compareWith(bPts[k])) {
                                continue bPoints;
                            }
                        }
                        bPts[bSz] = pt;
                        bSz++;
                    }
                }

                for (Point aP : aPts) {
                    for (Point bP : bPts) {
                        sharedV += aP.compareWith(bP) ? 1 : 0;
                    }
                }

                if (sharedV == 2) {
                    validEdges++;
                } else if (sharedV == 0) {
                    UnitVector aSN = a.getSurfaceNormal();
                    UnitVector bSN = b.getSurfaceNormal();
                    if (!(Math.abs(aSN.getI() + bSN.getI()) <= 0.0001)
                            || !(Math.abs(aSN.getJ() + bSN.getJ()) <= 0.0001)
                            || !(Math.abs(aSN.getK() + bSN.getK()) <= 0.0001)) {
                        this.mesh = invalid;
                        return;
                    }
                } else {
                    this.mesh = invalid;
                    return;
                }
            }

            if (validEdges != 4) {
                this.mesh = invalid;
                return;
            }
        }

        this.mesh = faces;
    }

    public Cube() {
        this(new Face(), new Face(), new Face(), new Face(), new Face(), new Face());
    }

    public Face[] getMesh() {
        return this.mesh;
    }

    public String toString() {
        if (this.mesh[0].toString().equals("{InvalidFace}")
                || this.mesh[1].toString().equals("{InvalidFace}")
                || this.mesh[2].toString().equals("{InvalidFace}")
                || this.mesh[3].toString().equals("{InvalidFace}")
                || this.mesh[4].toString().equals("{InvalidFace}")
                || this.mesh[5].toString().equals("{InvalidFace}")) {
            return "|InvalidCube|";
        }

        return String.format(
                "|C%s%s%s%s%s%s|",
                this.mesh[0].toString(),
                this.mesh[1].toString(),
                this.mesh[2].toString(),
                this.mesh[3].toString(),
                this.mesh[4].toString(),
                this.mesh[5].toString()
        );
    }
}
