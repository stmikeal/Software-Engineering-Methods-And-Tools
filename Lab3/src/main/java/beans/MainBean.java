package beans;

import dao.PointDAO;
import data.Point;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "mainBean")
@SessionScoped
public class MainBean implements Serializable {

  private float y = 0;
  private float x = 0;
  private int r = 1;
  private List<Point> pointList;
  private final PointDAO pointDAO = new PointDAO();
  private final DatabaseHandler databaseHandler = new DatabaseHandler();
  private LocalDateTime dateTime;

  public MainBean() {
  pointList = new ArrayList<>();
  }

  public String getDateTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
    return LocalDateTime.now().format(formatter);
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setR1() {
    r = 1;
  }

  public void setR2() {
    r = 2;
  }

  public void setR3() {
    r = 3;
  }

  public void setR4() {
    r = 4;
  }

  public void setR5() {
    r = 5;
  }

  public void setR(int r) {
    this.r = r;
  }

  public int getR() {
    return r;
  }

  public List<Point> getPointList() {
    try {
      System.out.println(pointList.get(0));
    }catch (Exception e){}
    System.out.println(Arrays.toString(pointList.toArray()));
    System.out.println("Hello world");
    return pointList;
  }

  public void setPointList(ArrayList<Point> pointList) {
    this.pointList = pointList;
  }

  public void checkPoint() {
    Point point = new Point(getX(), getY(), getR(), System.currentTimeMillis());
    point.setId(pointDAO.getMaxId() + 1);
    pointDAO.add(point);
    pointList = pointDAO.findAll(point.getSession_id());
  }

  public void nothing() {

  }

  public void clearPoints() {
    Point point = new Point(1,2,3,4);
    pointDAO.clear(point.getSession_id());
    pointList.clear();
  }

}

