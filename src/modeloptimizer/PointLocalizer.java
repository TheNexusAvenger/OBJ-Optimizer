/*
 * Localizes points from 3D space to 2D space and back.
 * Base logic by blobbyblog (https://devforum.roblox.com/t/projecting-3d-points-into-2d-space/133385/8)
 *
 * @author: TheNexusAvenger
 * @date: 6/7/2018
 */

package modeloptimizer;

import vectors.*;
import geometry.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PointLocalizer {
    private Vector3 originPoint;
    private Vector3 rightVector;
    private Vector3 backVector;
    private Vector3 upVector;
    private HashMap<Vector2,Vector3> vectorLookupMap;

    /**
     * Creates a point localizer.
     *
     * @param point1 first point to base localization.
     * @param point2 second point to base localization.
     * @param point3 third point to base localization.
     */
    public PointLocalizer(Vector3 point1,Vector3 point2,Vector3 point3) {
        this.vectorLookupMap = new HashMap<>();
        this.originPoint = point1;

        // Get vectors from origin.
        this.rightVector = (point1.subtract(point2)).getUnitVector();
        this.backVector = this.rightVector.cross(point3.subtract(point2)).getUnitVector();
        this.upVector = backVector.cross(rightVector).getUnitVector();
    }

    /**
     * Creates a point localizer.
     *
     * @param triangle triangle to base localization.
     */
    public PointLocalizer(Triangle3D triangle) {
        this(triangle.point1,triangle.point2,triangle.point3);
    }

    /**
     * Returns a localized point (Vector2) from a Vector3.
     *
     * @param point point to project.
     */
    public Vector2 getLocalSpacePoint(Vector3 point) {
        // Get already transformed point if it exists.
        for (Vector2 returnPoint : this.vectorLookupMap.keySet()) {
            if (this.vectorLookupMap.get(returnPoint).isClose(point,0.001)) {
                return returnPoint;
            }
        }

        // Transform to local space.
        double newX = (point.subtract(this.originPoint)).dot(this.rightVector);
        double newY = (point.subtract(this.originPoint)).dot(this.upVector);

        Vector2 localVector = new Vector2(newX,newY);
        this.vectorLookupMap.put(localVector,point);
        return localVector;
    }

    /**
     * Returns an unlocalized point (Vector3) from a Vector2.
     * Currently requires a Vector2 to be converted from the Vector3.
     *
     * @param point point to convert back.
     */
    public Vector3 getGlobalSpacePoint(Vector2 point) {
        // If the point was already calculated, return it.
        Vector3 globalPoint = this.vectorLookupMap.get(point);
        if (globalPoint != null) {
            return globalPoint;
        }

        // If it hasn't been calculated, extrapolate the point.
        return this.originPoint.add(this.rightVector.multiply(point.x)).add(this.upVector.multiply(point.y));
    }

    /**
     * Converts Triangle3Ds to Triangles.
     *
     * @param triangles triangles to convert.
     */
    public ArrayList<Triangle> convertTrianglesTo2D(ArrayList<Triangle3D> triangles) {
        ArrayList<Triangle> convertedTriangles = new ArrayList<>();

        // Convert triangles.
        for (Triangle3D triangle : triangles) {
            Vector2 point1 = this.getLocalSpacePoint(triangle.point1);
            Vector2 point2 = this.getLocalSpacePoint(triangle.point2);
            Vector2 point3 = this.getLocalSpacePoint(triangle.point3);
            convertedTriangles.add(new Triangle(point1,point2,point3));
        }

        return convertedTriangles;
    }

    /**
     * Converts Triangles to Triangle3Ds.
     *
     * @param triangles triangles to convert.
     */
    public ArrayList<Triangle3D> convertTrianglesTo3D(ArrayList<Triangle> triangles) {
        ArrayList<Triangle3D> convertedTriangles = new ArrayList<>();

        // Convert triangles.
        for (Triangle triangle : triangles) {
            Vector3 point1 = this.getGlobalSpacePoint(triangle.point1);
            Vector3 point2 = this.getGlobalSpacePoint(triangle.point2);
            Vector3 point3 = this.getGlobalSpacePoint(triangle.point3);
            convertedTriangles.add(new Triangle3D(point1,point2,point3));
        }

        return convertedTriangles;
    }
}
