package diasil.intersect;

import diasil.Diasil;
import diasil.material.LightRay;
import diasil.material.Material;
import diasil.math.geometry3.Box3;
import diasil.math.geometry3.Normal3;
import diasil.math.geometry3.Point3;
import diasil.math.geometry3.Ray3;
import diasil.math.geometry3.Vector3;

public class XYPlane extends Shape
{
	private float width, height;
	public XYPlane(float width, float height, Material m)
	{
		super(m);
		this.width = width;
		this.height = height;
	}
	public Intersection getIntersection(Ray3 rw)
	{
		Ray3 ro = toObjectSpace(rw);
		float t = -ro.O.Z/ro.D.Z;
        if (LightRay.isValid(t))
        {
			float px = ro.O.X + t*ro.D.X;
			float py = ro.O.Y + t*ro.D.Y;
			if (px > -width && px < width
					&& py > -height && py < height)
			{
				return new Intersection(rw, ro, t, this);
			}
        }
        return null;
	}
	
	public SurfaceGeometry getSurfaceGeometry(Intersection it)
	{
		Normal3 n = new Normal3(0.0f, 0.0f, 1.0f);
		float u = (width+it.Pobject.X)/(2*width);
		float v = (height+it.Pobject.Y)/(2*height);
		Vector3 dpdu = new Vector3(1.0f, 0.0f, 0.0f);
		Vector3 dpdv = new Vector3(0.0f, 1.0f, 0.0f);
		Vector3 dndu = new Vector3(0.0f, 0.0f, 0.0f);
		Vector3 dndv = new Vector3(0.0f, 0.0f, 0.0f);
		return SurfaceGeometry.toWorldSpace(it, n, u, v, dpdu, dpdv, this);
	}
	public Point3 sampleSurface(float u1, float u2)
	{
		Point3 r = new Point3((2.0f*u1 - 1.0f)*(width/2),
								(2.0f*u2 - 1.0f)*(height/2),
								0.0f);
		return toWorldSpace(r);
	}
	public Box3 getBoundingBox()
	{
		Box3 r = new Box3(new Point3(-width, -height, 0.0f), new Point3(width, height, 0.0f));
		return toWorldSpace(r);
	}	
}