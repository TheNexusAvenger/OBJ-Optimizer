# OBJ-Optimizer
Optimizes the triangles in OBJ files. The intended workflow is for exporting OBJ files from Roblox made with the Solid Modeling engine, and re-importing them after running the optimizer. This was written in Java instead of Lua since it can directly interface with file systems, and works cross platform.

# Usage
When running a jar artifact or directly with BatchOptimizer.java, it will look for .obj files in a folder named "Meshes" in the directory of the project or artifact. It will be created if it doesn't exist, and stop if it can't be created (ex: file named Meshes). The optimize will recursively scan the directory and optimize .objs, with new files having the ending of _optimized.obj. The material files, .mtl's, are not affected.
