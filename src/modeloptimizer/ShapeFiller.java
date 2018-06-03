package modeloptimizer;

import geometry.*;
import vectors.*;
import java.util.ArrayList;
import java.util.HashSet;

public class ShapeFiller {
    public static ArrayList<Line> getDrawLinesFromShapes(ArrayList<Shape> shapes) {
        // Add the lines.
        ArrayList<Line> lines = new ArrayList<>();
        HashSet<Vector2> points = new HashSet <>();
        for (Shape shape : shapes) {
            lines.addAll(shape.lines);

            for (Line line : shape.lines) {
                points.add(line.start);
                points.add(line.end);
            }
        }

        // Add the inner lines
        Shape completeShape = new Shape(lines);
        for (Vector2 point1 : points) {
            for (Vector2 point2 : points) {
                if (!point1.equals(point2)) {
                    Vector2 midPoint = point1.add(point2).divide(2);

                    // If the midpoint is in the shape and the line doesn't intersect any other lines, add the line.
                    if (completeShape.pointInShape(midPoint)) {
                        Line newLine = new Line(point1,point2);
                        boolean intersects = false;

                        for (Line line : lines) {
                            if (!line.canConnect(newLine) && line.getIntersectionPoint(newLine) != null) {
                                intersects = true;
                                break;
                            }
                        }

                        if (!intersects && !lines.contains(newLine)) {
                            lines.add(newLine);
                        }
                    }
                }
            }
        }

        return lines;
    }

    /**
     * Returns the line that connects 2 points in a set of lines, if any.
     *
     * @param point1 first point to connect.
     * @param point2 second point to connect.
     * @param lines lines to search in.
     */
    public static Line getConnectingLine(Vector2 point1,Vector2 point2,ArrayList<Line> lines) {
        for (Line line : lines) {
            if ((point1.equals(line.start) && point2.equals(line.end)) || (point1.equals(line.end) && point2.equals(line.start))) {
                return line;
            }
        }

        return null;
    }

    /**
     * Creates triangles from the given set of lines.
     *
     * @param lines lines to use.
     */
    public static ArrayList<Triangle> getTrianglesFromLines(ArrayList<Line> lines) {
        ArrayList<Triangle> triangles = new ArrayList<>();

        // Find triangles.
        for (Line line1 : lines) {
            Vector2 point1 = line1.start;
            Vector2 point2 = line1.end;

            for (Line line2 : lines) {
                if (!line1.equals(line2)) {
                    Vector2 point3 = line2.start;
                    Vector2 point4 = line2.end;

                    if (point1.equals(point3)) {
                        Line line3 = getConnectingLine(point2,point4,lines);
                        Triangle newTriangle = new Triangle(point1,point2,point4);
                        if (line3 != null && !triangles.contains(newTriangle)) {
                            triangles.add(newTriangle);
                        }
                    } else if (point1.equals(point4)) {
                        Line line3 = getConnectingLine(point2,point3,lines);
                        Triangle newTriangle = new Triangle(point1,point2,point3);
                        if (line3 != null && !triangles.contains(newTriangle)) {
                            triangles.add(newTriangle);
                        }
                    } else if (point2.equals(point3)) {
                        Line line3 = getConnectingLine(point1,point4,lines);
                        Triangle newTriangle = new Triangle(point1,point2,point4);
                        if (line3 != null && !triangles.contains(newTriangle)) {
                            triangles.add(newTriangle);
                        }
                    } else if (point2.equals(point4)) {
                        Line line3 = getConnectingLine(point1,point3,lines);
                        Triangle newTriangle = new Triangle(point1,point2,point3);
                        if (line3 != null && !triangles.contains(newTriangle)) {
                            triangles.add(newTriangle);
                        }
                    }
                }
            }
        }

        return triangles;
    }

    /*

        for i,Line1 in pairs(LocalSpaceLines) do
            local Point1,Point2 = Line1[1],Line1[2]
            for j,Line2 in pairs(LocalSpaceLines) do
                if i ~= j then
                    local Point3,Point4 = Line2[1],Line2[2]
                    if Point1 == Point3 then
                        local Line3 = FindConnectingLine(Point2,Point4)
                        if Line3 then
                            AddTriangle(Point1,Point2,Point4)
                        end
                    elseif Point1 == Point4 then
                        local Line3 = FindConnectingLine(Point2,Point3)
                        if Line3 then
                            AddTriangle(Point1,Point2,Point3)
                        end
                    elseif Point2 == Point3 then
                        local Line3 = FindConnectingLine(Point1,Point4)
                        if Line3 then
                            AddTriangle(Point1,Point2,Point4)
                        end
                    elseif Point2 == Point4 then
                        local Line3 = FindConnectingLine(Point1,Point3)
                        if Line3 then
                            AddTriangle(Point1,Point2,Point3)
                        end
                    end
                end
            end
        end

        return Triangles
    end
     */
}
