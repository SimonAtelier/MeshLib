# MeshLib

## MeshCreators

### CubeCreator

This creator creates a mesh representing a standard cube with eight vertices, twelve edges, and six faces.

```java
import mesh.creator.primitives.CubeCreator;
```

#### Parameters
 * radius - The radius of the cube.

#### Example

```java
Mesh mesh;
CubeCreator creator = new CubeCreator();
creator.setRadius(2.0f);
mesh = creator.create();
```