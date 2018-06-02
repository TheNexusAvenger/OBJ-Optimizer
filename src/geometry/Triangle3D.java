/*
 * Represents a 3D triangle. Offers very little functionality other than storage.
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package geometry;

import vectors.*;

public class Triangle3D {
    public Vector3 point1;
    public Vector3 point2;
    public Vector3 point3;

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
    }
}
