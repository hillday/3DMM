package model;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3b;
import javax.vecmath.Point3d;

import org.ejml.data.DenseMatrix64F;

public class Face {

	private DenseMatrix64F shape;
	private DenseMatrix64F texture;
	private int[] faceIndices;
	private int vertexCount;
	
	public Face(DenseMatrix64F shape, DenseMatrix64F texture, int[] faceIndices) {
		if(shape.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for shape should be 3 (X,Y,Z).");
		if(texture.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for texture should be 3 (R,G,B)");
		if(shape.getNumRows() <= 0 || texture.getNumRows() <= 0 || faceIndices.length <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(shape.getNumRows() != texture.getNumRows())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");
		
		this.shape = shape;
		this.texture = texture;
		this.faceIndices = faceIndices;
		this.vertexCount = shape.numRows;
	}

	public DenseMatrix64F getShapeMatrix() {
		return shape;
	}

	public void setShapeMatrix(DenseMatrix64F shape) {
		this.shape = shape;
	}

	public DenseMatrix64F getTextureMatrix() {
		return texture;
	}

	public void setTextureMatrix(DenseMatrix64F texture) {
		this.texture = texture;
	}
	
	public IndexedTriangleArray getGeometry() {
		Point3d[] points = new Point3d[vertexCount];
		Color3b[] colors = new Color3b[vertexCount];
		
		for(int x = 0; x < vertexCount; x++) {
			points[x] = new Point3d(shape.get(x, 0), shape.get(x, 1), shape.get(x, 2));
			colors[x] = new Color3b((byte) texture.get(x, 0), (byte) texture.get(x, 1), (byte) texture.get(x, 2));
		}
		
		IndexedTriangleArray face = new IndexedTriangleArray(vertexCount,GeometryArray.COORDINATES | GeometryArray.COLOR_3, faceIndices.length);
		
		face.setCoordinateIndices(0, faceIndices);
		face.setCoordinates(0, points);
		face.setColors(0,colors);
		face.setColorIndices(0, faceIndices);
		return face;
	}
	
	public Shape3D getShape() {
		return new Shape3D(this.getGeometry());
	}
	
}
