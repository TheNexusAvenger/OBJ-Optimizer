/*
 * Reads OBJ files.
 *
 * @author: TheNexusAvenger
 * @date: 6/9/2018
 */

package wavefront;

import vectors.*;
import wavefront.OBJTriangle.OBJTriangleVertex;
import java.util.ArrayList;
import java.util.HashMap;

public class OBJReader {
    public static String DEFAULT_GROUP = "__UNSPECIFIED";
    private String currentGroup;
    public ArrayList<String> mtlLibs;
    public ArrayList<Vector3W> vertices;
    public ArrayList<Vector3> vertexNormals;
    public ArrayList<Vector2W> vertexTextures;
    public HashMap<String,ArrayList<OBJTriangle>> triangles;
    public HashMap<String,String> mtls;



    /**
     * Creates an OBJ parser.
     *
     * @param objSource source of the obj to parse.
     */
    public OBJReader(String objSource) {
        this.currentGroup = DEFAULT_GROUP;
        this.vertices = new ArrayList<>();
        this.vertexNormals = new ArrayList<>();
        this.vertexTextures = new ArrayList<>();
        this.mtlLibs = new ArrayList<>();
        this.triangles = new HashMap<>();
        this.mtls = new HashMap<>();

        // Goes through lines and parses the OBJ.
        for (String line : objSource.split("\n")) {
            String lineType[] = line.split(" ",2);

            if (lineType.length >= 2) {
                String instruction = lineType[0].toLowerCase();
                String remainder = lineType[1];

                if (instruction.equals("mtllib")) {
                    // If it is mtllib, add the library file.
                    this.mtlLibs.add(remainder);
                } else if (instruction.equals("g")) {
                    // If it is g, set the current group.
                    this.currentGroup = remainder;
                    this.initializeCurrentGroup();
                } else if (instruction.equals("v")) {
                    // If it is v, add the vertex.
                    this.initializeCurrentGroup();

                    String[] points = remainder.split(" ");
                    if (points.length == 4) {
                        this.vertices.add(new Vector3W(Double.parseDouble(points[0]),Double.parseDouble(points[1]),Double.parseDouble(points[2]),Double.parseDouble(points[3])));
                    } else if (points.length == 3) {
                        this.vertices.add(new Vector3W(Double.parseDouble(points[0]),Double.parseDouble(points[1]),Double.parseDouble(points[2])));
                    } else {
                        System.out.println("Unprocessed vertex: " + line);
                    }
                } else if (instruction.equals("vn")) {
                    // If it is vn, add the normal.
                    this.initializeCurrentGroup();

                    String[] points = remainder.split(" ");
                    if (points.length == 3) {
                        this.vertexNormals.add(new Vector3(Double.parseDouble(points[0]),Double.parseDouble(points[1]),Double.parseDouble(points[2])).getUnitVector());
                    } else {
                        System.out.println("Unprocessed normal: " + line);
                    }
                } else if (instruction.equals("vt")) {
                    // If it is vt, add the texture vertex.
                    this.initializeCurrentGroup();

                    String[] points = remainder.split(" ");
                    if (points.length == 3) {
                        this.vertexTextures.add(new Vector2W(Double.parseDouble(points[0]), Double.parseDouble(points[1]), Double.parseDouble(points[2])));
                    } else if (points.length == 2) {
                        this.vertexTextures.add(new Vector2W(Double.parseDouble(points[0]),Double.parseDouble(points[1])));
                    } else {
                        System.out.println("Unprocessed texture vertex: " + line);
                    }
                } else if (instruction.equals("f")) {
                    // If it is f, add the triangle.
                    this.initializeCurrentGroup();

                    String[] points = remainder.split(" ");
                    if (points.length == 3) {
                        this.triangles.get(this.currentGroup).add(new OBJTriangle(this.getOBJPoint(points[0]),this.getOBJPoint(points[1]),this.getOBJPoint(points[2])));
                    } else {
                        System.out.println("Unprocessed triangle: " + line);
                    }
                } else if (instruction.equals("usemtl")) {
                    // If it is usemtl, set the mtl.
                    this.initializeCurrentGroup();
                    this.mtls.put(this.currentGroup,remainder);
                } else if (!instruction.equals("#") && !instruction.equals(" ") && !instruction.equals("\t")) {
                    // If wasn't processed and isn't a comment, print out that it wasn't processed.
                    System.out.println("Unprocessed line: " + line);
                }
            }
        }
    }

    /**
     * Initializes the current group by setting HashMap entries if they don't exist.
     */
    private void initializeCurrentGroup() {
        if (!this.mtls.containsKey(this.currentGroup)) {
            this.triangles.put(this.currentGroup,new ArrayList<>());
        }
    }

    /**
     * Returns an OBJTriangleVertex from the given input string.
     *
     * @param vertexData data of the vertex from the obj file, formatted as "###", "###/###", "###/###/###", or "###/###".
     */
    private OBJTriangleVertex getOBJPoint(String vertexData) {
        String[] numbers = vertexData.split("/");
        OBJTriangleVertex vertex = new OBJTriangleVertex();

        // Add normal.
        if (numbers.length >= 3) {
            int normalIndex = Integer.parseInt(numbers[2]) - 1;
            vertex.normal = this.vertexNormals.get(normalIndex);
        }

        // Add texture.
        if (numbers.length >= 2) {
            if (!numbers[2].equals("")) {
                int textureIndex = Integer.parseInt(numbers[1]) - 1;
                vertex.texture = this.vertexTextures.get(textureIndex);
            }
        }

        // Add vertex.
        if (numbers.length >= 1) {
            int vertexIndex = Integer.parseInt(numbers[0]) - 1;
            vertex.vertex = this.vertices.get(vertexIndex);
        }

        return vertex;
    }

    /**
     * Returns a list of all the faces to optimize.
     */
    public HashMap<String,ArrayList<ArrayList<OBJTriangle>>> getTriangles() {
        HashMap<String,HashMap<Vector3,ArrayList<ArrayList<OBJTriangle>>>> shapesByName = new HashMap<>();

        // Get triangles for each group and normal.
        for (String groupName : this.triangles.keySet()) {
            if (shapesByName.get(groupName) == null) {
                shapesByName.put(groupName,new HashMap<>());
            }
            HashMap<Vector3,ArrayList<ArrayList<OBJTriangle>>> shapesForName = shapesByName.get(groupName);

            for (OBJTriangle triangle : this.triangles.get(groupName)) {
                // Get the normal used by at least 2 points.
                Vector3 normal = null;
                if (triangle.point1.normal != OBJTriangle.DEFAULT_NORMAL && (triangle.point1.normal.equals(triangle.point2.normal) || triangle.point1.normal.equals(triangle.point3.normal))) {
                    normal = triangle.point1.normal;
                } else if (triangle.point2.normal != OBJTriangle.DEFAULT_NORMAL && (triangle.point2.normal.equals(triangle.point1.normal) || triangle.point1.normal.equals(triangle.point3.normal))) {
                    normal = triangle.point2.normal;
                } else if (triangle.point3.normal != OBJTriangle.DEFAULT_NORMAL && (triangle.point3.normal.equals(triangle.point1.normal) || triangle.point1.normal.equals(triangle.point2.normal))) {
                    normal = triangle.point3.normal;
                }

                // If a normal wasn't found, get the normal of the points.
                if (normal == null) {
                    normal = triangle.point1.normal;
                    if (normal == OBJTriangle.DEFAULT_NORMAL) {
                        normal = triangle.point2.normal;
                        if (normal == OBJTriangle.DEFAULT_NORMAL) {
                            normal = triangle.point3.normal;
                        }
                    }
                }

                // Add triangle to shape if any valid.
                if (shapesForName.get(normal) == null) {
                    shapesForName.put(normal, new ArrayList<>());
                }

                boolean triangleInShape = false;
                for (ArrayList<OBJTriangle> triangleSet : shapesForName.get(normal)) {
                    OBJTriangle firstTriangle = triangleSet.get(0);
                    if (firstTriangle.point1.isCoPlanar(triangle.point1.vertex)) {
                        triangleSet.add(triangle);
                        triangleInShape = true;
                        break;
                    }
                }

                // Add new shape if triangle wasn't placed.
                if (!triangleInShape) {
                    ArrayList<OBJTriangle> newShape = new ArrayList<>();
                    newShape.add(triangle);
                    shapesForName.get(normal).add(newShape);
                }
            }
        }

        // Create list to return.
        HashMap<String,ArrayList<ArrayList<OBJTriangle>>> triangles = new HashMap<>();
        for (String groupName : shapesByName.keySet()) {
            HashMap<Vector3,ArrayList<ArrayList<OBJTriangle>>> shapesWithNormals = shapesByName.get(groupName);
            ArrayList<ArrayList<OBJTriangle>> trianglesGroup = new ArrayList<>();
            triangles.put(groupName,trianglesGroup);

            for (Vector3 normal : shapesWithNormals.keySet()) {
                trianglesGroup.addAll(shapesWithNormals.get(normal));
            }
        }

        return triangles;
    }
}
