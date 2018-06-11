/*
 * Optimizes a folder of OBJs.
 *
 * @author: TheNexusAvenger
 * @date: 6/10/2018
 */

import wavefront.OBJOptimizer;

import java.io.File;

public class BatchOptimizer {
    public static String DEFAULT_DIRECTROY = "Meshes";
    public static String EXTENSION = ".obj";
    public static String OPTIMIZED_FILE_ENDING = "_optimized.obj";

    /**
     * Optimizes a given OBJ file and creates an output in the same directory.
     *
     * @param file file to optimize.
     */
    public static void optimizeFile(File file) {
        // Determine if the file is an OBJ.
        if (file.exists() && !file.isDirectory()) {
            String fileName = file.getName();
            String baseName = fileName.substring(0,fileName.length() - EXTENSION.length());
            String extention = fileName.substring(fileName.length() - EXTENSION.length()).toLowerCase();

            // Optimize the OBJ.
            if (extention.equals(EXTENSION)) {
                long startTime = System.currentTimeMillis();
                File targetFile = new File(file.getParent() + "\\" + baseName + OPTIMIZED_FILE_ENDING);

                OBJOptimizer parser = new OBJOptimizer(file);
                parser.writeOBJToFile(targetFile);

                // Get elapsed time.
                long endTime = System.currentTimeMillis();
                System.out.println("Mesh \"" + fileName + "\" parsed in " + ((endTime - startTime) / 1000.0) + " seconds");
            }
        }
    }

    /**
     * Scans the given directory and optimizes OBJs.
     *
     * @param directory directory to scan.
     */
    public static void scanDirectory(File directory) {
        if (directory.isDirectory()) {
            File files[] = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        optimizeFile(file);
                    }
                }
            }
        }
    }

    /**
     * Runs batch optimizer.
     */
    public static void runBatchOptimizer() {
        // Find directory and create it if it doesn't exist.
        File file = new File(DEFAULT_DIRECTROY);
        if (!file.exists()) {
            file.mkdir();
            file = new File(DEFAULT_DIRECTROY);
        } else if (!file.isDirectory()) {
            throw new RuntimeException(DEFAULT_DIRECTROY + " already exists but isn't a directory.");
        }

        // Run recursive scan and optimize OBJs.
        if (file.exists()) {
            scanDirectory(file);
        } else {
            throw new RuntimeException(DEFAULT_DIRECTROY + " doesn't exist and wasn't created.");
        }
    }

    public static void main(String[] args) {
        runBatchOptimizer();
    }
}
