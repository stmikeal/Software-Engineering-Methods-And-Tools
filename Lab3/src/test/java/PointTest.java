import static org.junit.Assert.assertFalse;
import data.Point;
import org.junit.Test;

public class PointTest {
  @Test
  public void testHello() {
    Point point = new Point();
    assertFalse(point.calculate(10, 10, 1));
  }
}