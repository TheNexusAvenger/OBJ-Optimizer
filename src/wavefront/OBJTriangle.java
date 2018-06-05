package wavefront;

import geometry.Triangle3D;
import vectors.Vector3;

public class OBJTriangle extends Triangle3D {
    public static Vector3 DEFAULT_NORMAL = new Vector3(0,5,0);
    public OBJTriangleVertex point1;
    public OBJTriangleVertex point2;
    public OBJTriangleVertex point3;



    /**
     * Class for storing the vertices.
     */
    public static class OBJTriangleVertex {
        public Vector3W vertex;
        public Vector3 normal = DEFAULT_NORMAL;
        public Vector2W texture;

        /**
         * Returns whether a given point is coplanar.
         *
         * @param otherPoint point to compare.
         */
        public boolean isCoPlanar(Vector3 otherPoint) {
            return ((otherPoint.subtract(this.vertex)).dot(this.normal) == 0);
        }

        /**
         * Returns whether another object is equal.
         *
         * @param object the other object to compare.
         */
        @Override
        public boolean equals(Object object) {
            if (!(object instanceof OBJTriangleVertex)) {
                return false;
            }

            OBJTriangleVertex otherVertex = (OBJTriangleVertex) object;
            return (this.vertex.equals(otherVertex.vertex) && this.normal.equals(otherVertex.normal) && this.texture.equals(otherVertex.texture));
        }

        /**
         * Returns the hash code of the Vector3.
         */
        @Override
        public int hashCode() {
            return (this.vertex.toString() + this.normal.toString() + this.texture.toString()).hashCode();
        }
    }



    /**
     * Creates an OBJTriangle.
     *
     * @param point1 first point.
     * @param point2 second point.
     * @param point3 third point.
     */
    public OBJTriangle(OBJTriangleVertex point1,OBJTriangleVertex point2,OBJTriangleVertex point3) {
        super(point1.vertex,point2.vertex,point3.vertex);
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }
}
