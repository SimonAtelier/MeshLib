# Core Classes
```java
package mesh;
```
#### Mesh3D
```java
import mesh.Mesh3D;
```

#### Face3D
```java
import mesh.Face3D;
```

#### Edge3D
```java
import mesh.Edge3D;
```

# Mesh Creators

## Platonic Solid Creators
```java
package mesh.creator.platonic;
```
This package contains creators to create the five *Platonic Solids*.
The following table gives a quick overview:

| Solid        | Creator             | Faces | Edges | Vertices |
|--------------|---------------------|:-----:|:-----:|:--------:|
| Tetrahedron  | TetrahedronCreator  | 4     | 6     | 4        |
| Hexahedron   | HexahedronCreator   | 6     | 12    | 8        |
| Octahedron   | OctahedronCreator   | 8     | 12    | 6        |
| Dodecahedron | DodecahedronCreator | 12    | 30    | 20       |
| Icosahedron  | IcosahedronCreator  | 20    | 30    | 12       |

#### Tetrahedron Creator
Creates a *Tetrahedron* with four faces, six edges, and four vertices.
The deault length of the edges is 2√2.
* **Radius** The radius of the Tetrahedron (1.0f by default).

```java
import mesh.Mesh3D;
import mesh.creator.platonic.TetrahedronCreator;

TetrahedronCreator creator = new TetrahedronCreator();
Mesh3D mesh = creator.create();
```
[Wikipedia-Link](https://en.wikipedia.org/wiki/Tetrahedron)

#### Hexahedron Creator
Creates a *Hexahedron* with six faces, twelve edges, and four vertices.
The length of each edge is 2 by default.
* **Radius** The radius of the Hexahedron (1.0f by default). The length of each edge is twice as large as the given radius.

```java
import mesh.Mesh3D;
import mesh.creator.platonic.HexahedronCreator;

Mesh3D mesh;
HexahedronCreator creator = new HexahedronCreator();
creator.setRadius(2.0f);
mesh = creator.create();
```
[Wikipedia-Link](https://en.wikipedia.org/wiki/Cube)

#### Octahedron Creator
```java
import mesh.Mesh3D;
import mesh.creator.platonic.OctahedronCreator;

OctahedronCreator creator = new OctahedronCreator();
Mesh3D mesh = creator.create();
```
#### Dodecahedron Creator
```java
import mesh.Mesh3D;
import mesh.creator.platonic.DodecahedronCreator;

DodecahedronCreator creator = new DodecahedronCreator();
Mesh3D mesh = creator.create();
```
#### Icosahedron Creator
```java
import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;

IcosahedronCreator creator = new IcosahedronCreator();
Mesh3D mesh = creator.create();
```
