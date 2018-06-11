/*
 * Writes OBJ files.
 *
 * @author: TheNexusAvenger
 * @date: 6/5/2018
 */

package wavefront;

import vectors.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class OBJWriter {
    private OBJReader objReader;

    /**
     * Creates an OBJ Writer.
     *
     * @param objReader reader that read the OBJ.
     */
    public OBJWriter(OBJReader objReader) {
        this.objReader = objReader;
    }

    /**
     * Merges triangles that can be merged.
     *
     * @param faces groups and faces of triangles.
     */
    public HashMap<String,ArrayList<OBJTriangle>> mergeTriangles(HashMap<String,ArrayList<ArrayList<OBJTriangle>>> faces) {
        HashMap<String,ArrayList<OBJTriangle>> finalFaces = new HashMap<>();

        // Combine faces into groups.
        for (String groupName : faces.keySet()) {
            ArrayList<ArrayList<OBJTriangle>> faceGroup = faces.get(groupName);
            ArrayList<OBJTriangle> combinedTriangles = new ArrayList<>();
            finalFaces.put(groupName,combinedTriangles);

            for (ArrayList<OBJTriangle> triangles : faceGroup) {
                combinedTriangles.addAll(triangles);
            }
        }

        // Merge triangles.
        for (String groupName : finalFaces.keySet()) {
            ArrayList<OBJTriangle> triangles = finalFaces.get(groupName);
            HashSet<OBJTriangle> removedTriangles = new HashSet<>();

            for (OBJTriangle triangle1 : triangles) {
                if (!removedTriangles.contains(triangle1)) {
                    boolean stopMerge = false;
                    for (OBJTriangle triangle2 : triangles) {
                        if (stopMerge) {
                            break;
                        }

                        if (!removedTriangles.contains(triangle2) && triangle1 != triangle2) {
                            Line3D triangle1Line1 = new Line3D(triangle1.point1,triangle1.point2);
                            Line3D triangle1Line2 = new Line3D(triangle1.point1,triangle1.point3);
                            Line3D triangle1Line3 = new Line3D(triangle1.point2,triangle1.point3);
                            Line3D triangle2Line1 = new Line3D(triangle2.point1,triangle2.point2);
                            Line3D triangle2Line2 = new Line3D(triangle2.point1,triangle2.point3);
                            Line3D triangle2Line3 = new Line3D(triangle2.point2,triangle2.point3);
                            ArrayList<Line3D> triangle1Lines = new ArrayList<>();
                            triangle1Lines.add(triangle1Line1);
                            triangle1Lines.add(triangle1Line2);
                            triangle1Lines.add(triangle1Line3);
                            ArrayList<Line3D> triangle2Lines = new ArrayList<>();
                            triangle2Lines.add(triangle2Line1);
                            triangle2Lines.add(triangle2Line2);
                            triangle2Lines.add(triangle2Line3);

                            for (Line3D line1 : triangle1Lines) {
                                for (Line3D line2 : triangle1Lines) {
                                    if (line1 != line2) {
                                        for (Line3D line3 : triangle2Lines) {
                                            for (Line3D line4 : triangle2Lines) {
                                                if (line3 != line4) {
                                                    if (line1.equals(line3) && line2.canMergeWithLine(line4)) {
                                                        OBJTriangle.OBJTriangleVertex newPoint2 = line4.getUncommonPoint(line2);
                                                        OBJTriangle.OBJTriangleVertex newPoint3 = line2.getUncommonPoint(line4);
                                                        if (newPoint3.vertex.subtract(newPoint2.vertex).magnitude > (Math.max(line2.length,line4.length))) {
                                                            triangle1.setPoint1(line1.getUncommonPoint(line2));
                                                            triangle1.setPoint2(newPoint2);
                                                            triangle1.setPoint3(newPoint3);
                                                            triangle1.updateWinding();
                                                            removedTriangles.add(triangle2);
                                                        } else {
                                                            if (line4.length > line2.length) {
                                                                removedTriangles.add(triangle1);
                                                                stopMerge = true;
                                                            } else {
                                                                removedTriangles.add(triangle2);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (OBJTriangle triangle : triangles) {
                if (triangle.area == 0) {
                    removedTriangles.add(triangle);
                }
            }

            // Remove triangles
            for (OBJTriangle triangle : removedTriangles) {
                triangles.remove(triangles.indexOf(triangle));
            }
        }

        return finalFaces;
    }

    /**
     * Creates new source for OBJ.
     *
     * @param faces faces of the OBJ to write.
     */
    public String getOBJSource(HashMap<String,ArrayList<OBJTriangle>> faces) {
        String finalFormat = "";

        // Write MTLLibs
        for (String mtlLib : this.objReader.mtlLibs) {
            finalFormat += "mtllib " + mtlLib + "\n";
        }
        finalFormat += "\n";

        String firstUsedHeader = null;
        if (this.objReader.triangles.keySet().size() >= 1) {
            firstUsedHeader = (String) this.objReader.triangles.keySet().toArray()[0];
            if (firstUsedHeader != OBJReader.DEFAULT_GROUP) {
                finalFormat += "g " + this.objReader.triangles.keySet().toArray()[0] + "\n\n";
            }
        }

        // Store all the vertices, texture vertices, and normals.
        HashSet<Vector3W> vertices = new HashSet<>();
        HashSet<Vector2W> vertexTextures = new HashSet<>();
        HashSet<Vector3> vertexNormals = new HashSet<>();

        for (String groupName : faces.keySet()) {
            ArrayList<OBJTriangle> triangleSet = faces.get(groupName);

            for (OBJTriangle triangle : triangleSet) {
                vertices.add(triangle.point1.vertex);
                if (triangle.point1.texture != null) { vertexTextures.add(triangle.point1.texture); }
                if (triangle.point1.normal != OBJTriangle.DEFAULT_NORMAL) { vertexNormals.add(triangle.point1.normal); }
                vertices.add(triangle.point2.vertex);
                if (triangle.point2.texture != null) { vertexTextures.add(triangle.point2.texture); }
                if (triangle.point2.normal != OBJTriangle.DEFAULT_NORMAL) { vertexNormals.add(triangle.point2.normal); }
                vertices.add(triangle.point3.vertex);
                if (triangle.point3.texture != null) { vertexTextures.add(triangle.point3.texture); }
                if (triangle.point3.normal != OBJTriangle.DEFAULT_NORMAL) { vertexNormals.add(triangle.point3.normal); }
            }
        }

        ArrayList<Vector3W> verticesList = new ArrayList<>(vertices);
        ArrayList<Vector2W> vertexTexturesList = new ArrayList<>(vertexTextures);
        ArrayList<Vector3> vertexNormalsList = new ArrayList<>(vertexNormals);

        // Write vertices.
        for (Vector3W vertex : vertices) {
            finalFormat += "v " + vertex.x + " " + vertex.y + " " + vertex.z;
            if (vertex.w != 1.00) {
                finalFormat += " " + vertex.w + "\n";
            } else {
                finalFormat += "\n";
            }
        }
        finalFormat += "\n";

        // Write texture vertices.
        for (Vector2W vertex : vertexTextures) {
            finalFormat += "vt " + vertex.x + " " + vertex.y;
            if (vertex.w != 1.00) {
                finalFormat += " " + vertex.w + "\n";
            } else {
                finalFormat += "\n";
            }
        }
        finalFormat += "\n";

        // Write normal vertices.
        for (Vector3 vertex : vertexNormals) {
            finalFormat += "vn " + vertex.x + " " + vertex.y + " " + vertex.z + "\n";
        }
        finalFormat += "\n";

        // Write the triangles.
        for (String groupName : faces.keySet()) {
            ArrayList<OBJTriangle> triangleSet = faces.get(groupName);

            // Write group.
            if (groupName != firstUsedHeader) {
                finalFormat += "g " + groupName + "\n\n";
            }

            // Write MTL to use, if any.
            if (objReader.mtls.get(groupName) != null) {
                finalFormat += "usemtl " + objReader.mtls.get(groupName) + "\n\n";
            }

            // Write faces.
            for (OBJTriangle triangle : triangleSet) {
                finalFormat += "f ";

                // Write point 1.
                finalFormat += (verticesList.indexOf(triangle.point1.vertex) + 1
                );
                if (triangle.point1.texture != null || triangle.point1.normal != OBJTriangle.DEFAULT_NORMAL) {
                    finalFormat += "/";
                    if (triangle.point1.texture != null) {
                        finalFormat += (vertexTexturesList.indexOf(triangle.point1.texture) + 1);
                    }
                    if (triangle.point1.normal != OBJTriangle.DEFAULT_NORMAL) {
                        finalFormat += "/" + (vertexNormalsList.indexOf(triangle.point1.normal) + 1);
                    }
                }

                // Write point 2.
                finalFormat += " " + (verticesList.indexOf(triangle.point2.vertex) + 1
                );
                if (triangle.point2.texture != null || triangle.point2.normal != OBJTriangle.DEFAULT_NORMAL) {
                    finalFormat += "/";
                    if (triangle.point2.texture != null) {
                        finalFormat += (vertexTexturesList.indexOf(triangle.point2.texture) + 1);
                    }
                    if (triangle.point2.normal != OBJTriangle.DEFAULT_NORMAL) {
                        finalFormat += "/" + (vertexNormalsList.indexOf(triangle.point2.normal) + 1);
                    }
                }

                // Write point 3.
                finalFormat += " " + (verticesList.indexOf(triangle.point3.vertex) + 1
                );
                if (triangle.point3.texture != null || triangle.point3.normal != OBJTriangle.DEFAULT_NORMAL) {
                    finalFormat += "/";
                    if (triangle.point3.texture != null) {
                        finalFormat += (vertexTexturesList.indexOf(triangle.point3.texture) + 1);
                    }
                    if (triangle.point3.normal != OBJTriangle.DEFAULT_NORMAL) {
                        finalFormat += "/" + (vertexNormalsList.indexOf(triangle.point3.normal) + 1);
                    }
                }

                finalFormat += "\n";
            }
            finalFormat += "\n";
        }

        return finalFormat.substring(0,finalFormat.length() - 1);
    }
}
