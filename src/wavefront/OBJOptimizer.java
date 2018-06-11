/*
 * Optimizes OBJ files.
 *
 * @author: TheNexusAvenger
 * @date: 6/10/2018
 */

package wavefront;

import geometry.Line;
import geometry.Shape;
import geometry.Triangle;
import geometry.Triangle3D;
import modeloptimizer.PointLocalizer;
import modeloptimizer.ShapeCreator;
import modeloptimizer.ShapeFiller;
import vectors.Vector3;
import wavefront.OBJTriangle.OBJTriangleVertex;

import javax.imageio.stream.IIOByteBuffer;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class OBJOptimizer {
    private OBJReader objParser;

    /**
     * Creates an OBJOptimizer.
     *
     * @param objSource source of the OBJ.
     */
    public OBJOptimizer(String objSource) {
        this.objParser = new OBJReader(objSource);
    }

    /**
     * Creates an OBJOptimizer.
     *
     * @param file file location of the OBJ.
     */
    public OBJOptimizer(File file) {
        String source = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                source += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            if (scanner != null) {
                scanner.close();
            }
            e.printStackTrace();
        }

        this.objParser = new OBJReader(source);
    }

    /**
     * Optimizes the triangles from the read OBJ.
     */
    private HashMap<String,ArrayList<ArrayList<OBJTriangle>>> getOptimizedTriangles() {
        HashMap<String,ArrayList<ArrayList<OBJTriangle>>> triangles = this.objParser.getTriangles();

        // Optimize triangles
        for (String groupName : triangles.keySet()) {
            ArrayList<ArrayList<OBJTriangle>> shapes = triangles.get(groupName);
            ArrayList<ArrayList<OBJTriangle>> newShapes = new ArrayList<>();
            triangles.put(groupName,newShapes);

            for (ArrayList<OBJTriangle> shapeTriangles : shapes) {
                // Remove zero width triangles.
                ArrayList<OBJTriangle> trianglesToRemove = new ArrayList<>();
                for (OBJTriangle triangle : shapeTriangles) {
                    if (triangle.area == 0.00) {
                        trianglesToRemove.add(triangle);
                    }
                }
                for (OBJTriangle triangle : trianglesToRemove) {
                    shapeTriangles.remove(shapeTriangles.indexOf(triangle));
                }

                if (shapeTriangles.size() > 0) {
                    // Store all points in a map.
                    HashMap<Vector3, OBJTriangleVertex> pointLookup = new HashMap<>();
                    ArrayList<Triangle3D> baseTriangles = new ArrayList<>();
                    for (OBJTriangle triangle : shapeTriangles) {
                        pointLookup.put(triangle.point1.vertex, triangle.point1);
                        pointLookup.put(triangle.point2.vertex, triangle.point2);
                        pointLookup.put(triangle.point3.vertex, triangle.point3);
                        baseTriangles.add(triangle);
                    }

                    // Get optimized triangles.
                    PointLocalizer localizer = new PointLocalizer(shapeTriangles.get(0));
                    ArrayList<Triangle> localSpaceTriangles = localizer.convertTrianglesTo2D(baseTriangles);

                    ShapeCreator shapeCreator = new ShapeCreator();
                    ArrayList<Shape> boundaryShapes = shapeCreator.getShapesFromTriangles(localSpaceTriangles);

                    ArrayList<Line> lines = ShapeFiller.getDrawLinesFromShapes(boundaryShapes);
                    ArrayList<Triangle> finalTriangles = ShapeFiller.getTrianglesFromLines(lines);
                    ArrayList<Triangle3D> finalTriangles3D = localizer.convertTrianglesTo3D(finalTriangles);

                    // Convert triangles back
                    ArrayList<OBJTriangle> newShape = new ArrayList<>();
                    for (Triangle3D triangle : finalTriangles3D) {
                        newShape.add(new OBJTriangle(pointLookup.get(triangle.point1), pointLookup.get(triangle.point2), pointLookup.get(triangle.point3)));
                    }
                    newShapes.add(newShape);
                }
            }
        }


        return triangles;
    }

    /**
     * Returns the triangle count for the given OBJ triangles.
     *
     * @param triangles triangles to count.
     */
    private int getTriangleCountOfFaces(HashMap<String,ArrayList<ArrayList<OBJTriangle>>> triangles) {
        int triangleCount = 0;
        for (String groupName : triangles.keySet()) {
            ArrayList<ArrayList<OBJTriangle>> shapeGroup = triangles.get(groupName);

            for (ArrayList<OBJTriangle> triangleSet : shapeGroup) {
                triangleCount += triangleSet.size();
            }
        }

        return triangleCount;
    }

    /**
     * Returns the triangle count for the given OBJ triangles.
     *
     * @param triangles triangles to count.
     */
    private int getTriangleCountOfGroups(HashMap<String,ArrayList<OBJTriangle>> triangles) {
        int triangleCount = 0;
        for (String groupName : triangles.keySet()) {
            ArrayList<OBJTriangle> triangleSet = triangles.get(groupName);
            triangleCount += triangleSet.size();
        }

        return triangleCount;
    }

    /**
     * Returns the final OBJ as a string.
     */
    public String getOptimizedOBJSource() {
        OBJWriter objWriter = new OBJWriter(this.objParser);
        // Get triangles and triangle count.
        int baseTriangleCount = getTriangleCountOfFaces(this.objParser.getTriangles());
        HashMap<String,ArrayList<ArrayList<OBJTriangle>>> triangles = this.getOptimizedTriangles();
        HashMap<String,ArrayList<OBJTriangle>> finalFaces = objWriter.mergeTriangles(triangles);
        int finalTriangleCount = getTriangleCountOfGroups(finalFaces);

        // Create source.
        String source = objWriter.getOBJSource(finalFaces);
        source = "# Old triangle count: " + baseTriangleCount + "\n" + source;
        source = "# New triangle count: " + finalTriangleCount + "\n" + source;

        return source;
    }

    /**
     * Writes the final OBJ to a specified destination file.
     *
     * @param fileLocation location to write the file.
     */
    public void writeOBJToFile(File fileLocation) {
        String source = getOptimizedOBJSource();

        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(fileLocation));
            printStream.print(source);
            printStream.close();
        } catch (FileNotFoundException e) {
            if (printStream != null) {
                printStream.close();
            }
            e.printStackTrace();
        }
    }

}
