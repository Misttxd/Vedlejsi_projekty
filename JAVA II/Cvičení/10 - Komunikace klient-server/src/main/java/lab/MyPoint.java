package lab;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPoint implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;

    public MyPoint multiply(double multiplier) {
        return new MyPoint(x * multiplier, y * multiplier);
    }

    public MyPoint add(MyPoint other) {
        return new MyPoint(x + other.x, y + other.y);
    }

    public MyPoint add(double dx, double dy) {
        return new MyPoint(x + dx, y + dy);
    }

    public MyPoint subtract(double dx, double dy) {
        return new MyPoint(x - dx, y - dy);
    }
}
