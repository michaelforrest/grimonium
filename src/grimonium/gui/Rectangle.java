package grimonium.gui;

public class Rectangle extends Point {
	public float width;
	public float height;

	public Rectangle(float $x, float $y, float $width, float $height) {
		super($x, $y);
		width = $width;
		height = $height;
	}

	public void setTopLeft(Point p) {
		x = p.x;
		y = p.y;
	}

	public Point topLeft() {
		return new Point(x, y);
	}

	public Point centre() {
		return new Point(x + (width / 2), y + (height / 2));
	}

	public float right() {
		return x + width;
	}

	public float bottom() {
		return y + height;
	}

	public Rectangle scaleFromMiddle(float amount) {
		float horizontal = amount * width / 2;
		float vertical = amount * height / 2;
		return new Rectangle(centre().x - horizontal, centre().y - vertical,
				2 * horizontal, 2 * vertical);
	}
}