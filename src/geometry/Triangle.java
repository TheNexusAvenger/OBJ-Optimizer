/*
 * Represents a 2D triangle.
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package geometry;

import vectors.*;

public class Triangle {
    public Vector2 point1;
    public Vector2 point2;
    public Vector2 point3;

    /**
     * Creates a triangle from 3 points.
     *
     * @param point1 the first point.
     * @param point2 the second point.
     * @param point3 the third point.
     */
    public Triangle(Vector2 point1,Vector2 point2,Vector2 point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    /**
     * Returns whether another object is equal.
     *
     * @param object the other object to compare.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Triangle)) {
            return false;
        }

        Triangle otherTriangle = (Triangle) object;
        if (otherTriangle.point1 == this.point1) {
            return (otherTriangle.point2 == this.point2 && otherTriangle.point3 == this.point3) || (otherTriangle.point3 == this.point2 && otherTriangle.point2 == this.point3);
        } else if (otherTriangle.point1 == this.point2) {
            return (otherTriangle.point2 == this.point1 && otherTriangle.point3 == this.point3) || (otherTriangle.point3 == this.point1 && otherTriangle.point2 == this.point3);
        } else if (otherTriangle.point1 == this.point3) {
            return (otherTriangle.point2 == this.point1 && otherTriangle.point3 == this.point3) || (otherTriangle.point3 == this.point2 && otherTriangle.point2 == this.point1);
        }

        return false;
    }

    /**
     * Returns the Triangle as a string.
     */
    @Override
    public String toString() {
        return "Triangle:\n\t" + this.point1 + "\n\t" + this.point2 + "\n\t" + this.point3;
    }
}
