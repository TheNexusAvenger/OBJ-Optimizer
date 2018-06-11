/*
 * Represents a 3D triangle. Offers very little functionality other than storage.
 *
 * @author: TheNexusAvenger
 * @date: 6/7/2018
 */

package geometry;

import vectors.*;

public class Triangle3D {
    public Vector3 point1;
    public Vector3 point2;
    public Vector3 point3;
    public double area;

    /**
     * Creates a triangle from 3 points.
     *
     * @param point1 the first point.
     * @param point2 the second point.
     * @param point3 the third point.
     */
    public Triangle3D(Vector3 point1,Vector3 point2,Vector3 point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;

        // Calculate area.
        this.updateArea();
    }

    /**
     * Updates the area of the triangle.
     */
    public void updateArea() {
        double distance1 = point1.subtract(point2).magnitude;
        double distance2 = point1.subtract(point3).magnitude;
        double distance3 = point2.subtract(point3).magnitude;
        double semiPerimeter = (distance1 + distance2 + distance3) / 2.00;
        this.area = Math.pow(semiPerimeter * (semiPerimeter - distance1) * (semiPerimeter - distance2) * (semiPerimeter - distance3),0.5);
    }

    /**
     * Sets the first point.
     *
     * @param newPoint point to set.
     */
    public void setPoint1(Vector3 newPoint) {
        this.point1 = newPoint;
        this.updateArea();
    }

    /**
     * Sets the second point.
     *
     * @param newPoint point to set.
     */
    public void setPoint2(Vector3 newPoint) {
        this.point2 = newPoint;
        this.updateArea();
    }

    /**
     * Sets the third point.
     *
     * @param newPoint point to set.
     */
    public void setPoint3(Vector3 newPoint) {
        this.point3 = newPoint;
        this.updateArea();
    }

    /**
     * Returns the Triangle as a string.
     */
    @Override
    public String toString() {
        return "Triangle:\n\t" + this.point1 + "\n\t" + this.point2 + "\n\t" + this.point3;
    }
}
