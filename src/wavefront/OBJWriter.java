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
     * Creates new source for OBJ.
     *
     * @param faces faces of the OBJ to write.
     */
    public String getOBJSource(HashMap<String,ArrayList<ArrayList<OBJTriangle>>> faces) {
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
            ArrayList<ArrayList<OBJTriangle>> groupFaces = faces.get(groupName);

            for (ArrayList<OBJTriangle> triangleSet : groupFaces) {
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
            ArrayList<ArrayList<OBJTriangle>> groupFaces = faces.get(groupName);

            // Write group.
            if (groupName != firstUsedHeader) {
                finalFormat += "g " + groupName + "\n\n";
            }

            // Write MTL to use, if any.
            if (objReader.mtls.get(groupName) != null) {
                finalFormat += "usemtl " + objReader.mtls.get(groupName) + "\n\n";
            }

            // Write faces.
            for (ArrayList<OBJTriangle> triangleSet : groupFaces) {
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
            }
            finalFormat += "\n";
        }

        return finalFormat.substring(0,finalFormat.length() - 1);
    }
}
