/*
 * Represents a 3D point.
 * API mirrors Roblox's API (http://wiki.roblox.com/index.php?title=API:Vector3), with the
 * exception of FromNormalId and FromAxis.
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package vectors;

public class Vector3 {
    public double x;
    public double y;
    public double z;
    public double magnitude;

    /**
     * Creates a new Vector3 with the given X and Y coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.magnitude = Math.pow(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2),0.5);
    }

    /**
     * Creates a blank Vector2 with the coordinates 0,0.
     */
    public Vector3() {
        this(0,0,0);
    }

    /**
     * Returns the unit vector of the Vector2.
     */
    public Vector3 getUnitVector() {
        return this.divide(this.magnitude);
    }

    /**
     * Performs the cross product with another Vector2.
     *
     * @param otherVector the other Vector2 to cross with.
     */
    public Vector3 cross(Vector3 otherVector) {
        double newX = (this.y * otherVector.z) - (this.z * otherVector.y);
        double newY = (this.z * otherVector.x) - (this.x * otherVector.z);
        double newZ = (this.x * otherVector.y) - (this.y * otherVector.x);

        return new Vector3(newX,newY,newZ);
    }

    /**
     *
     * Performs the dot product with another Vector3.
     *
     * @param otherVector the other Vector3 to dot with.
     */
    public double dot(Vector3 otherVector) {
        return (this.x * otherVector.x) + (this.y * otherVector.y) + (this.z * otherVector.z);
    }

    /**
     * Returns a Vector3 between the start and goal based on the alpha.
     *
     * @param goal the goal Vector3.
     * @param alpha alpha of the transformation. Should be between 0 and 1 inclusive.
     */
    public Vector3 lerp(Vector3 goal,double alpha) {
        double newX = this.x + ((goal.x - this.x) * alpha);
        double newY = this.y + ((goal.y - this.y) * alpha);
        double newZ = this.z + ((goal.z - this.z) * alpha);

        return new Vector3(newX,newY,newZ);
    }

    /**
     * Returns if the Vector3 is within the specified epsilon.
     */
    public boolean isClose(Vector3 otherVector,double epsilon) {
        return (this.subtract(otherVector).magnitude < epsilon);
    }

    /**
     * Returns if the Vector3 is within margin of error (0.000001).
     */
    public boolean isClose(Vector3 otherVector) {
        return isClose(otherVector,0.000001);
    }

    /**
     * Adds another Vector3 to the existing Vector3.
     *
     * @param otherVector The Vector3 to add.
     */
    public Vector3 add(Vector3 otherVector) {
        return new Vector3(this.x + otherVector.x,this.y + otherVector.y,this.z + otherVector.z);
    }

    /**
     * Subtracts another Vector3 to the existing Vector3.
     *
     * @param otherVector The Vector3 to subtract.
     */
    public Vector3 subtract(Vector3 otherVector) {
        return new Vector3(this.x - otherVector.x,this.y - otherVector.y,this.z - otherVector.z);
    }

    /**
     * Multiplies another Vector3 to the existing Vector3.
     *
     * @param otherVector The Vector3 to multiply.
     */
    public Vector3 multiply(Vector3 otherVector) {
        return new Vector3(this.x * otherVector.x,this.y * otherVector.y,this.z * otherVector.z);
    }

    /**
     * Multiplies a scalar to the existing Vector3.
     *
     * @param scalar The scalar to multiply.
     */
    public Vector3 multiply(double scalar) {
        return new Vector3(this.x * scalar,this.y * scalar,this.z * scalar);
    }

    /**
     * Divides another Vector3 to the existing Vector3.
     *
     * @param otherVector The Vector3 to divide.
     */
    public Vector3 divide(Vector3 otherVector) {
        return new Vector3(this.x / otherVector.x,this.y / otherVector.y,this.z / otherVector.z);
    }

    /**
     * Divides a scalar to the existing Vector3.
     *
     * @param scalar The scalar to divide.
     */
    public Vector3 divide(double scalar) {
        return new Vector3(this.x / scalar,this.y / scalar,this.z / scalar);
    }

    /**
     * Returns whether another object is equal.
     *
     * @param object the other object to compare.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vector3)) {
            return false;
        }

        Vector3 otherVector = (Vector3) object;
        return (this.x == otherVector.y) && (this.y == otherVector.y) && (this.z == otherVector.z);
    }

    /**
     * Returns the Vector2 as a string.
     */
    @Override
    public String toString() {
        return "Vector3(" + this.x + "," + this.y + "," + this.z + ")";
    }

    /**
     * Returns the hash code of the Vector3.
     */
    @Override
    public int hashCode() {
        return (Double.toString(this.x) + Double.toString(this.y) + Double.toString(this.z)).hashCode();
    }
}
