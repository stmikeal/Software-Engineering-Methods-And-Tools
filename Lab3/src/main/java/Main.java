import data.Point;

public class Main {

  public static void main(String[] args) {
    double x = 1;
    double y = 2;
    double r = 3;
    Point point = new Point(x, y, r);

    point.calculate(x, y, r);
    String answer = point.toString();
    System.out.println(answer);
  }
}