/*
 * Represents a 3D triangle while keeping the OBJ points.
 *
 * @author: TheNexusAvenger
 * @date: 6/5/2018
 */

package wavefront;

import geometry.Triangle3D;
import vectors.Vector3;

public class OBJTriangle extends Triangle3D {
    public static Vector3 DEFAULT_NORMAL = new Vector3(0,5,0);
    public OBJTriangleVertex point1;
    public OBJTriangleVertex point2;
    public OBJTriangleVertex point3;
    public double area;



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
            return (Math.abs((otherPoint.subtract(this.vertex)).dot(this.normal)) <= 0.0001);
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
            return this.vertex.hashCode();
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
        super(point1.vertex, point2.vertex, point3.vertex);

        // Calculate area.
        double distance1 = point1.vertex.subtract(point2.vertex).magnitude;
        double distance2 = point1.vertex.subtract(point3.vertex).magnitude;
        double distance3 = point2.vertex.subtract(point3.vertex).magnitude;
        double semiPerimeter = (distance1 + distance2 + distance3) / 2.00;
        this.area = Math.pow(semiPerimeter * (semiPerimeter - distance1) * (semiPerimeter - distance2) * (semiPerimeter - distance3),0.5);

        if (area == 0) {
            this.point1 = point1;
            this.point2 = point2;
            this.point3 = point3;
        } else {
            // Get calculated normal.
            Vector3 calculatedNormal = (point2.vertex.subtract(point1.vertex)).cross(point3.vertex.subtract(point1.vertex)).getUnitVector();

            // Flip point2 and point3 if it is incorrectly winded.
            if (calculatedNormal.subtract(point1.normal).magnitude < 1) {
                this.point1 = point1;
                this.point2 = point2;
                this.point3 = point3;
            } else {
                this.point1 = point1;
                this.point2 = point3;
                this.point3 = point2;
            }
        }
    }
}
