/*
 * Represents a 2D point.
 * API mirrors Roblox's API (http://wiki.roblox.com/index.php?title=Vector2)
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package vectors;

public class Vector2 {
    public double x;
    public double y;
    public double magnitude;

    /**
     * Creates a new Vector2 with the given X and Y coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitude = Math.pow(Math.pow(x,2) + Math.pow(y,2),0.5);
    }

    /**
     * Creates a blank Vector2 with the coordinates 0,0.
     */
    public Vector2() {
        this(0,0);
    }

    /**
     * Returns the unit vector of the Vector2.
     */
    public Vector2 getUnitVector() {
        return this.divide(this.magnitude);
    }

    /**
     * Performs the cross product with another Vector2.
     *
     * @param otherVector the other Vector2 to cross with.
     */
    public double cross(Vector2 otherVector) {
        return (this.x * otherVector.y) - (this.y * otherVector.x);
    }

    /**
     *
     * Performs the dot product with another Vector2.
     *
     * @param otherVector the other Vector2 to dot with.
     */
    public double dot(Vector2 otherVector) {
        return (this.x * otherVector.x) + (this.y * otherVector.y);
    }

    /**
     * Returns a Vector2 between the start and goal based on the alpha.
     *
     * @param goal the goal Vector2.
     * @param alpha alpha of the transformation. Should be between 0 and 1 inclusive.
     */
    public Vector2 lerp(Vector2 goal,double alpha) {
        return new Vector2(this.x + ((goal.x - this.x) * alpha),this.y + ((goal.y - this.y) * alpha));
    }

    /**
     * Returns if the Vector2 is within the specified epsilon.
     */
    public boolean isClose(Vector2 otherVector,double epsilon) {
        return (this.subtract(otherVector).magnitude < epsilon);
    }

    /**
     * Returns if the Vector2 is within margin of error (0.000001).
     */
    public boolean isClose(Vector2 otherVector) {
        return isClose(otherVector,0.000001);
    }

    /**
     * Adds another Vector2 to the existing Vector2.
     *
     * @param otherVector The Vector2 to add.
     */
    public Vector2 add(Vector2 otherVector) {
        return new Vector2(this.x + otherVector.x,this.y + otherVector.y);
    }

    /**
     * Subtracts another Vector2 to the existing Vector2.
     *
     * @param otherVector The Vector2 to subtract.
     */
    public Vector2 subtract(Vector2 otherVector) {
        return new Vector2(this.x - otherVector.x,this.y - otherVector.y);
    }

    /**
     * Multiplies another Vector2 to the existing Vector2.
     *
     * @param otherVector The Vector2 to multiply.
     */
    public Vector2 multiply(Vector2 otherVector) {
        return new Vector2(this.x * otherVector.x,this.y * otherVector.y);
    }

    /**
     * Multiplies a scalar to the existing Vector2.
     *
     * @param scalar The scalar to multiply.
     */
    public Vector2 multiply(double scalar) {
        return new Vector2(this.x * scalar,this.y * scalar);
    }

    /**
     * Divides another Vector2 to the existing Vector2.
     *
     * @param otherVector The Vector2 to divide.
     */
    public Vector2 divide(Vector2 otherVector) {
        return new Vector2(this.x / otherVector.x,this.y / otherVector.y);
    }

    /**
     * Divides a scalar to the existing Vector2.
     *
     * @param scalar The scalar to divide.
     */
    public Vector2 divide(double scalar) {
        return new Vector2(this.x / scalar,this.y / scalar);
    }

    /**
     * Returns whether another object is equal.
     *
     * @param object the other object to compare.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vector2)) {
            return false;
        }

        Vector2 otherVector = (Vector2) object;
        return (this.x == otherVector.y) && (this.y == otherVector.y);
    }

    /**
     * Returns the Vector2 as a string.
     */
    @Override
    public String toString() {
        return "Vector2(" + this.x + "," + this.y + ")";
    }
}
