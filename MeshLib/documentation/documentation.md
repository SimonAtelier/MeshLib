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

### PlaneCreator

This creator creates a single quad face, with four vertices, four edges, and one face. The created mesh
is not a three-dimensional object, because it is flat and has no thickness.

```java
import mesh.creator.primitives.PlaneCreator;
```

#### Parameters
 * radius - The radius of the plane.
 
 #### Example

```java
Mesh mesh;
PlaneCreator creator = new PlaneCreator();
creator.setRadius(2.0f);
mesh = creator.create();
```