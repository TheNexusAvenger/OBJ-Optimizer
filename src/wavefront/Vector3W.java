/*
 * Represents a 3D point. Adds an extra W for vertex colors.
 *
 * @author: TheNexusAvenger
 * @date: 6/5/2018
 */

package wavefront;

import vectors.Vector3;

public class Vector3W extends Vector3 {
    double w;

    /**
     * Creates a new Vector3 with the given X, Y, and Z coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @param w the vertex color multiplier.
     */
    public Vector3W(double x,double y,double z,double w) {
        super(x,y,z);
        this.w = w;
    }

    /**
     * Creates a new Vector3 with the given X, Y, and Z coordinates. Defaults w to 1.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public Vector3W(double x,double y,double z) {
        this(x,y,z,1.00);
    }

    /**
     * Creates a new Vector3. Defaults to 0,0,0,1.
     */
    public Vector3W() {
        this(0,0,0,1.00);
    }
}
