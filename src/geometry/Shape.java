/*
 * Represents a 2D shape for boundary checking.
 *
 * @author: TheNexusAvenger
 * @date: 6/5/2018
 */

package geometry;

import vectors.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

public class Shape {
    public ArrayList<Line> lines;

    /**
     * Creates a shape with a given set of boundary lines.
     *
     * @param lines list of lines in order.
     */
    public Shape(ArrayList<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    /**
     * Returns a list of X coordinates that align with the given Y coordinate.
     *
     * @param y the y coordinate to compare.
     */
    private ArrayList<Double> getXPointsForY(double y) {
        HashSet<Double> listOfXs = new HashSet<>();

        // Go through lines and find x coordinates that align with the y.
        for (Line line : this.lines) {
            if ((line.start.y <= y && line.end.y >= y) || (line.end.y <= y && line.start.y >= y)) {
                // Determine x
                double ratioY = (y - line.start.y) / (line.end.y - line.start.y);
                double newX = line.start.x + ((line.end.x - line.start.x) * ratioY);
                listOfXs.add(newX);
            }
        }

        // Assemble final list.
        ArrayList<Double> finalListOfXs = new ArrayList<>(listOfXs);
        Collections.sort(finalListOfXs);

        return finalListOfXs;
    }

    /**
     * Returns whether the given Vector2 is in the shape.
     *
     * @param point Vector2 to check if it is the bounds.
     */
    public boolean pointInShape(Vector2 point){
        ArrayList<Double> listOfXs = getXPointsForY(point.y);
        boolean inRegion = false;

        for (Double x : listOfXs) {
            if (x < point.x) {
                inRegion = !inRegion;
            }
        }

        return inRegion;
    }
}
