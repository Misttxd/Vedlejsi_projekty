package lab.cleaning;

import lab.Tools;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Třídu zjednodušte za použití anotací z knihovny Lombok.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class Box {

	private int x;
	private int y;
	private int width;
	private int height;

//	public Box(int x, int y, int width, int height) {
//		this.x = x;
//		this.y = y;
//		this.width = width;
//		this.height = height;
//	}

    public void moveToX(int x) {
		setX(x);
		setY(0);
	}

//	@Override
//	public String toString() {
//		return "Box [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
//	}

	public static Box generate() {
		return new Box(Tools.RANDOM.nextInt(20),
				Tools.RANDOM.nextInt(20),
				Tools.RANDOM.nextInt(5),
				Tools.RANDOM.nextInt(5));
	}

}
