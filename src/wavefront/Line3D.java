/*
 * Represents a 3D line.
 *
 * @author: TheNexusAvenger
 * @date: 6/9/2018
 */

package wavefront;

import wavefront.OBJTriangle.OBJTriangleVertex;

public class Line3D {
    public OBJTriangleVertex start;
    public OBJTriangleVertex end;
    public double length;

    /**
     * Creates a line.
     *
     * @param start first point.
     * @param end second point.
     */
    public Line3D(OBJTriangleVertex start, OBJTriangleVertex end) {
        this.start = start;
        this.end = end;
        this.length = end.vertex.subtract(start.vertex).magnitude;
    }

    /**
     * Returns whether a point is collinear with the line.
     *
     * @param point point to check.
     */
    public boolean isPointCollinear(OBJTriangleVertex point) {
        // If the point equals an endpoint return true.
        if (point.equals(this.start) || point.equals(this.end)) {
            return true;
        }

        // Return based on lengths.
        double distanceFromStart = (point.vertex.subtract(this.start.vertex)).magnitude;
        double distanceFromEnd = (point.vertex.subtract(this.end.vertex)).magnitude;

        if (this.length >= distanceFromStart && this.length >= distanceFromEnd) {
            return Math.abs((distanceFromStart + distanceFromEnd) - this.length) < 0.0000001;
        } else if (distanceFromStart  >= this.length && distanceFromStart >= distanceFromEnd) {
            return Math.abs((this.length + distanceFromEnd) - distanceFromStart) < 0.0000001;
        } else if (distanceFromEnd  >= this.length && distanceFromEnd >= distanceFromStart) {
            return Math.abs((this.length + distanceFromStart) - distanceFromEnd) < 0.0000001;
        }

        // Return false.
        return false;
    }

    /**
     * Returns whether another line can be merged with the line.
     *
     * @param line other line to merge.
     */
    public boolean canMergeWithLine(Line3D line) {
        if (this.start.equals(line.start) || this.end.equals(line.start)) {
            return this.isPointCollinear(line.end);
        } else if (this.start.equals(line.end) || this.end.equals(line.end)) {
            return this.isPointCollinear(line.start);
        }

        return false;
    }

    /**
     * Returns the uncommon point of 2 lines. Will return null if there is no common points.
     *
     * @param line other line to check.
     */
    public OBJTriangleVertex getUncommonPoint(Line3D line) {
        if (this.start.equals(line.start) || this.start.equals(line.end)) {
            return this.end;
        } else if (this.end.equals(line.start) || this.end.equals(line.end)) {
            return this.start;
        }

        return null;
    }

    /**
     * Returns whether another object is equal.
     *
     * @param object the other object to compare.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Line3D)) {
            return false;
        }

        Line3D otherLine = (Line3D) object;
        return (this.start.equals(otherLine.start) && this.end.equals(otherLine.end)) || (this.start.equals(otherLine.end) && this.end.equals(otherLine.start));
    }

    /**
     * Returns the Line as a string.
     */
    @Override
    public String toString() {
        return this.start.vertex + " <-> " + this.end.vertex;
    }
}
