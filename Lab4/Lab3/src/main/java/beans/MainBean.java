
package beans;

import mbean.PointMXBean;
import mbean.TimeMXBean;
import tools.Point;
import tools.PointDB;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.management.*;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Named("mainBean")
@ApplicationScoped
public class MainBean implements Serializable {

    private String x = "0";
    private String y = "0";
    private String r = "1";
    public static List<Point> points;
    private static long startTime = 0;
    private static long endTime = 0;
    private final PointDB pointDB = new PointDB();
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    PointMXBean mbean = new PointMXBean();

    public MainBean() throws NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {
        System.out.println("start_init");
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        System.out.println("start_init");
        ObjectName name = new ObjectName("mbean:type=PointMXBean");
        ObjectName name2 = new ObjectName("mbean:type=TimeMXBean");
        System.out.println("start_init");
        TimeMXBean mbean2 = new TimeMXBean();
        System.out.println("start_init");
        try {
            mbs.registerMBean(mbean, name);
            mbs.registerMBean(mbean2, name2);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("middle_init");
        points = new ArrayList<>();
        String idString = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId();
        System.out.println(idString);
        pointDB.findAll();
        System.out.println("end_init");
    }

    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return LocalDateTime.now().format(formatter);
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getR() {
        return r;
    }

    public List<Point> getPoints() {
        return points;
    }


    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public void setR(String r) {
        this.r = r;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void clearPoints() {
        points.clear();
        executor.execute(pointDB::clear);
    }

    public void addPoint() {
        Point point = new Point(x, y, r);
        executor.execute(() -> pointDB.add(point));
        points.add(point);
        if (startTime == 0)
            startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        mbean.threeMissInRow();
    }

    public static long meanTime(){
        return (endTime-startTime)/(points.size()!=0?points.size():1);
    }

    public void showClearMessage() {
    }
}