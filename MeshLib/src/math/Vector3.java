package math;

public class Vector3 {

	private static final Vector3 UNIT_X = new Vector3(1, 0, 0);
	private static final Vector3 UNIT_Y = new Vector3(0, 1, 0);
	private static final Vector3 UNIT_Z = new Vector3(0, 0, 1);

	private float x;
	private float y;
	private float z;

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 subtract(Vector3 other) {
		float x = this.x - other.x;
		float y = this.y - other.y;
		float z = this.z - other.z;
		return new Vector3(x, y, z);
	}

	public float length() {
		if (this.equals(UNIT_X))
			return 1;
		if (this.equals(UNIT_Y))
			return 1;
		if (this.equals(UNIT_Z))
			return 1;
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

}
