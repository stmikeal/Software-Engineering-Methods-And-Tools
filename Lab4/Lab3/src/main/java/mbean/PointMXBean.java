package mbean;

import beans.MainBean;
import tools.Point;
import tools.PointDB;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.ArrayList;
import java.util.List;

public class PointMXBean extends NotificationBroadcasterSupport implements TemplatePointMXBean {

    int sequenceNumber = 1;

    @Override
    public long getHits() {
        ;
        return MainBean.points.stream().filter(point -> "Hitted".equals(point.getHit())).count();
    }

    @Override
    public long getMiss() {
        //threeMissInRow();
        return MainBean.points.stream().filter(point -> "Miss".equals(point.getHit())).count();
    }

    public void threeMissInRow() {
        List<Point> points = MainBean.points;
        if (points.size() > 2 && "Miss".equals(points.get(points.size() - 1).getHit()) &&
                "Miss".equals(points.get(points.size() - 2).getHit()) &&
                "Miss".equals(points.get(points.size() - 3).getHit())) {
            Notification n = new AttributeChangeNotification(this,
                    sequenceNumber++, System.currentTimeMillis(),
                    "Too much miss, buy glasses!", "ThreeMiss", "int",
                    2, 3);

            sendNotification(n);
        }
    }

    @Override
    public long getShoots() {
        return MainBean.points.size();
    }
}
