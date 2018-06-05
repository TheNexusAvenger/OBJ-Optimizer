/*
 * Represents a 2D point. Adds an extra W for vertex colors.
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package wavefront;

import vectors.Vector2;

public class Vector2W extends Vector2 {
    double w;

    /**
     * Creates a new Vector2 with the given X and Y coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param w the vertex color multiplier.
     */
    public Vector2W(double x, double y, double w) {
        super(x,y);
        this.w = w;
    }

    /**
     * Creates a new Vector2 with the given X and Y coordinates. Defaults w to 0.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Vector2W(double x, double y) {
        this(x,y,0.00);
    }

    /**
     * Creates a new Vector2. Defaults to 0,0,0.
     */
    public Vector2W() {
        this(0,0,0.00);
    }
}
