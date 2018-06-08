/*
 * Creates shape boundaries for a given set of triangles.
 *
 * @author: TheNexusAvenger
 * @date: 6/6/2018
 */

package modeloptimizer;

import geometry.*;
import vectors.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

public class ShapeCreator {
    private HashMap<Vector2,ArrayList<Double[]>> anglesCovered;
    private HashMap<Vector2,HashMap<Double,ArrayList<Vector2>>> anglesToPoints;

    /**
     * Creates a ShapeCreator.
     */
    public ShapeCreator() {
        this.anglesCovered = new HashMap<>();
        this.anglesToPoints = new HashMap<>();
    }

    /**
     * Adds the covered angles.
     *
     * @param pointFrom point that is calculated.
     * @param pointTo1 first other point.
     * @param pointTo2 second other poind.
     */
    private void addAnglesOfCoverage(Vector2 pointFrom,Vector2 pointTo1,Vector2 pointTo2) {
        // Do nothing if any points are the same.
        if (pointFrom.equals(pointTo1) || pointFrom.equals(pointTo2) || pointTo1.equals(pointTo2)) {
            return;
        }

        Vector2 midPoint = pointTo1.add(pointTo2).divide(2);
        // Add hash entries if they don't already exist.
        if (this.anglesCovered.get(pointFrom) == null) {
            this.anglesCovered.put(pointFrom,new ArrayList<>());
            this.anglesToPoints.put(pointFrom,new HashMap<>());
        }

        // Calculate angles and swap points if needed.
        double angleToPoint1 = Math.atan2(pointTo1.y - pointFrom.y,pointTo1.x - pointFrom.x) % (Math.PI * 2.00);
        if (angleToPoint1 < 0) { angleToPoint1 += (Math.PI * 2); }
        double angleToPoint2 = Math.atan2(pointTo2.y - pointFrom.y,pointTo2.x - pointFrom.x) % (Math.PI * 2.00);
        if (angleToPoint2 < 0) { angleToPoint2 += (Math.PI * 2); }

        if (angleToPoint1 == 0 && angleToPoint2 == Math.PI * 2.00) {

        }

        double angleToMidpoint = Math.atan2(midPoint.y - pointFrom.y,midPoint.x - pointFrom.x) % (Math.PI * 2.00);
        if (angleToMidpoint < 0) { angleToMidpoint += (Math.PI * 2); }
        if (angleToPoint1 > angleToPoint2) {
            double tempAngleStore = angleToPoint1;
            angleToPoint1 = angleToPoint2;
            angleToPoint2 = tempAngleStore;

            Vector2 tempPointStore = pointTo1;
            pointTo1 = pointTo2;
            pointTo2 = tempPointStore;
        }

        // Add lines for angles to hash map.
        if (this.anglesToPoints.get(pointFrom).get(angleToPoint1) == null) {
            this.anglesToPoints.get(pointFrom).put(angleToPoint1,new ArrayList<>());
        }
        this.anglesToPoints.get(pointFrom).get(angleToPoint1).add(pointTo1);
        if (this.anglesToPoints.get(pointFrom).get(angleToPoint2) == null) {
            this.anglesToPoints.get(pointFrom).put(angleToPoint2,new ArrayList<>());
        }
        this.anglesToPoints.get(pointFrom).get(angleToPoint2).add(pointTo2);

        // Add covered angles.
        if (angleToPoint1 < angleToMidpoint && angleToPoint2 > angleToMidpoint) {
            Double anglePair[] = {angleToPoint1,angleToPoint2};
            this.anglesCovered.get(pointFrom).add(anglePair);
        } else {
            Double anglePair1[] = {0.00,angleToPoint1};
            this.anglesCovered.get(pointFrom).add(anglePair1);
            Double anglePair2[] = {angleToPoint2,Math.PI * 2};
            this.anglesCovered.get(pointFrom).add(anglePair2);
        }
    }

    /**
     * Merges the covered angles.
     *
     */
    private void mergeAnglesOfCoverage() {
        for (Vector2 point : this.anglesCovered.keySet()) {
            ArrayList<Double[]> anglesCovered = this.anglesCovered.get(point);

            // Sort by the lowest angle.
            anglesCovered.sort(new Comparator<Double[]>() {
                @Override
                public int compare(Double[] o1, Double[] o2) {
                    if (o1[0] < o2[0]) {
                        return -1;
                    } else if (o1[0] > o2[0]) {
                        return 1;
                    }
                    return 0;
                }
            });

            // Merge angles.
            ArrayList<Double[]> newAnglesCovered = new ArrayList<>();
            for (Double[] angleBound : anglesCovered) {
                double startAngle = angleBound[0];
                double endAngle = angleBound[1];

                boolean merged = false;
                for (Double[] angleBoundToMerge : newAnglesCovered) {
                    double startMergeAngle = angleBoundToMerge[0];
                    double endMergeAngle = angleBoundToMerge[1];

                    if (startAngle >= startMergeAngle && startAngle <= endMergeAngle) {
                        if (endAngle >= endMergeAngle) {
                            angleBoundToMerge[1] = endAngle;
                        }
                        merged = true;
                        break;
                    }
                }

                if (!merged) {
                    newAnglesCovered.add(angleBound);
                }
            }
            this.anglesCovered.put(point,newAnglesCovered);
        }
    }

    /**
     * Returns a hash map containing the angles of the lines for the points.
     */
    private HashMap<Vector2,ArrayList<Double>> getPointAngles() {
        HashMap<Vector2,ArrayList<Double>> anglePoints = new HashMap<>();

        // Get angles of all the points.
        for (Vector2 point : this.anglesCovered.keySet()) {
            ArrayList<Double[]> anglePairs = this.anglesCovered.get(point);
            double minAngle = 7.00;
            double maxAngle = -1.00;
            boolean nonTerminatingAngleExists = false;

            // Get minimum and maximum angles.
            for (Double[] angleBound : anglePairs) {
                double startAngle = angleBound[0];
                double endAngle = angleBound[1];

                if (minAngle > startAngle) {
                    minAngle = startAngle;
                }

                if (maxAngle < endAngle) {
                    maxAngle = endAngle;
                }

                if (startAngle != 0 || endAngle != Math.PI * 2.00) {
                    nonTerminatingAngleExists = true;
                }
            }

            // Add angles if the point is needed.
            boolean ignoreTerminatingLines = (minAngle == 0 && maxAngle == Math.PI * 2.00);
            boolean externalPoint = (!ignoreTerminatingLines || nonTerminatingAngleExists);
            if (externalPoint) {
                ArrayList<Double> neededAngles = new ArrayList<>();

                for (Double[] angleBound : anglePairs) {
                    Double startAngle = angleBound[0];
                    Double endAngle = angleBound[1];

                    if (!ignoreTerminatingLines || (startAngle != 0 && startAngle != Math.PI * 2.00)) {
                        neededAngles.add(startAngle);
                    }
                    if (!ignoreTerminatingLines || (endAngle != 0 && endAngle != Math.PI * 2.00)) {
                        neededAngles.add(endAngle);
                    }
                }
                anglePoints.put(point,neededAngles);
            }
        }

        return anglePoints;
    }

    /**
     * Returns the lines from the given points and angles.
     *
     * @param pointAngles points and angles map to determine the lines.
     */
    private ArrayList<Line> getLinesFromPointsAndAngles(HashMap<Vector2,ArrayList<Double>> pointAngles) {
        ArrayList<Line> boundaryLines = new ArrayList<>();

        // Add lines for the angles.
        for (Vector2 point : pointAngles.keySet()) {
            ArrayList<Double> angles = pointAngles.get(point);

            for (Double angle : angles) {
                ArrayList<Vector2> connectedPoints = this.anglesToPoints.get(point).get(angle);

                if (connectedPoints != null) {
                    for (Vector2 connectedPoint : connectedPoints) {
                        Line newLine = new Line(point,connectedPoint);

                        if (pointAngles.get(connectedPoint) != null && !boundaryLines.contains(newLine)) {
                            boundaryLines.add(newLine);
                        }
                    }
                }
            }
        }

        return boundaryLines;
    }

    /**
     * Merges lines that are collinear.
     *
     * @param lines lines to merge.
     */
    private ArrayList<Line> mergeLines(ArrayList<Line> lines) {
        // Remove lines that a length of 0 (start = end).
        for (int i = lines.size() - 1; i >= 0; i--) {
            Line line = lines.get(i);
            if (line.start.equals(line.end)) {
                lines.remove(i);
            }
        }

        // Combine lines if possible.
        ArrayList<Line> linesToRemove = new ArrayList<>();
        for (Line line1 : lines) {
            for (Line line2 : lines) {
                if (!(line1 == line2) && !linesToRemove.contains(line1) && !linesToRemove.contains(line2)) {
                    if (Math.abs(line1.slope - line2.slope) < 0.001 && line1.canConnect(line2)) {
                        linesToRemove.add(line2);

                        // Change line to max length.
                        double line2StartOnLine1 = line1.getPositionOnLine(line2.start);
                        double line2EndOnLine1 = line1.getPositionOnLine(line2.end);

                        if (line2StartOnLine1 < 0) {
                            line1.start = line2.start;
                        } else if (line2StartOnLine1 > 1) {
                            line1.end = line2.start;
                        }

                        if (line2EndOnLine1 < 0) {
                            line1.start = line2.end;
                        } else if (line2EndOnLine1 > 1) {
                            line1.end = line2.end;
                        }
                    }
                }
            }
        }

        // Remove lines.
        for (Line line : linesToRemove) {
            lines.remove(lines.indexOf(line));
        }
        return lines;
    }

    /**
     * Creates the bounding shapes.
     *
     * @param boundingLines the lines to bound the shapes.
     */
    private ArrayList<Shape> createShapes(ArrayList<Line> boundingLines) {
        boundingLines = this.mergeLines(boundingLines);
        ArrayList<Shape> shapes = new ArrayList<>();
        Vector2 lastPoint = null;
        ArrayList<Line> currentShape = null;

        // Create shapes until no more lines to connect.
        while (boundingLines.size() > 0) {
            if (lastPoint == null) {
                // If there is no shape, start a new one.
                Line newLine = boundingLines.get(0);
                lastPoint = newLine.end;
                boundingLines.remove(0);
                currentShape = new ArrayList<>();
                currentShape.add(newLine);
            } else {
                // If the shape exists, add the next line if any.
                Line nextLine = null;

                for (int i = 0; i < boundingLines.size(); i++) {
                    Line line = boundingLines.get(i);
                    if (line.start.equals(lastPoint)) {
                        lastPoint = line.end;
                        nextLine = line;
                        currentShape.add(line);
                        boundingLines.remove(i);
                        break;
                    } else if (line.end.equals(lastPoint)) {
                        lastPoint = line.start;
                        nextLine = line;
                        currentShape.add(line);
                        boundingLines.remove(i);
                        break;
                    }
                }

                // If the next line doesn't exist, reset the state for the shape.
                if (nextLine == null) {
                    lastPoint = null;
                    shapes.add(new Shape(currentShape));
                    currentShape = null;
                }
            }
        }

        // If there is another shape to add, create it.
        if (currentShape != null) {
            shapes.add(new Shape(currentShape));
        }

        return shapes;
    }

    /**
     * Creates the boundary shapes for a given set of triangles.
     *
     * @param triangles set if input triangles.
     */
    public ArrayList<Shape> getShapesFromTriangles(ArrayList<Triangle> triangles) {
        // Get covered angles.
        for (Triangle triangle : triangles) {
            this.addAnglesOfCoverage(triangle.point1,triangle.point2,triangle.point3);
            this.addAnglesOfCoverage(triangle.point2,triangle.point1,triangle.point3);
            this.addAnglesOfCoverage(triangle.point3,triangle.point1,triangle.point2);
        }
        this.mergeAnglesOfCoverage();

        // Get bounding lines and create shapes.
        HashMap<Vector2,ArrayList<Double>> pointAngles = getPointAngles();
        ArrayList<Line> boundaryLines = getLinesFromPointsAndAngles(pointAngles);
        return createShapes(boundaryLines);
    }
}
