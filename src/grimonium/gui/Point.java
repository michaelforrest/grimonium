package grimonium.gui;

public class Point {
	public float x;
	public float y;

	public Point(float $x, float $y) {
		x = $x;
		y = $y;
	}

	public boolean within(Rectangle area) {
		return x > area.x && x < area.right() && y > area.y
				&& y < area.bottom();
	}

	public Point add(Point p) {
		return new Point(x + p.x, y + p.y);
	}
}
