/*
 * Represents a 2D line.
 *
 * @author: TheNexusAvenger
 * @date: 6/1/2018
 */

package geometry;

import vectors.*;

public class Line {
    public Vector2 start;
    public Vector2 end;
    public double length;
    public double slope;

    /**
     * Creates a line.
     *
     * @param start
     * @param end
     */
    public Line(Vector2 start,Vector2 end) {
        this.start = start;
        this.end = end;
        this.length = end.subtract(start).magnitude;

        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;

        if (Math.abs(deltaX) < 0.001) {
            this.slope = Double.MAX_VALUE;
        } else {
            this.slope = deltaY/deltaX;
        }
    }

    /**
     * Determines the relative position on the line of a given point. Assumes that the point is collinear.
     *
     * @param point the point to calculate for.
     */
    public double getPositionOnLine(Vector2 point) {
        double distanceFromStart = (point.subtract(this.start)).magnitude;
        double distanceFromEnd = (point.subtract(this.end)).magnitude;

        if (distanceFromStart + distanceFromEnd > 1 && distanceFromStart < distanceFromEnd) {
            return -distanceFromStart;
        }
        return distanceFromStart;
    }

    /**
     * Returns whether another line can connect.
     *
     * @param otherLine the line to check
     */
    public boolean canConnect(Line otherLine) {
        return this.start.equals(otherLine.start) || this.end.equals(otherLine.end) || this.start.equals(otherLine.end) || this.end.equals(otherLine.start);
    }

    /**
     * Returns whether the 2 lines can be merged.
     *
     * @param otherLine line to check if it is collinear.
     */
    public boolean canMerge(Line otherLine) {
        if (!canConnect(otherLine)) {
            return false;
        }

        return Math.abs(this.slope - otherLine.slope) < 0.001;
    }

    /**
     * Returns the point where the given line intersects, or null if there is none.
     *
     * @param otherLine the line to check for an intersection.
     */
    public Vector2 getIntersectionPoint(Line otherLine) {
        Vector2 deltaMainLine = this.end.subtract(this.start);
        Vector2 deltaOtherLine = otherLine.end.subtract(this.end);

        double mainCross = deltaMainLine.cross(deltaOtherLine);
        double coefficient1 = this.start.subtract(otherLine.start).cross(deltaMainLine)/mainCross;
        double coefficient2 = this.start.subtract(otherLine.start).cross(deltaOtherLine)/mainCross;

        if (coefficient1 >= 0 && coefficient1 <= 1 && coefficient2 >= 0 && coefficient2 <= 1) {
            return this.start.lerp(end,coefficient1);
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
        if (!(object instanceof Line)) {
            return false;
        }

        Line otherLine = (Line) object;
        return (this.start.equals(otherLine.start) && this.end.equals(otherLine.end)) || (this.start.equals(otherLine.end) && this.end.equals(otherLine.start));
    }

    /**
     * Returns the Line as a string.
     */
    @Override
    public String toString() {
        return this.start + " <-> " + this.end;
    }
}
