package dao;

import data.Point;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.query.Query;
import utils.HibernateFactory;

public class PointDAO {


  public void add(Point point) {
    Session session = HibernateFactory.getSessionFactory().openSession();
    Transaction tx1 = session.beginTransaction();
    session.save(point);
    tx1.commit();
    session.close();
  }

  public void clear(String session_id) {
    Session session = HibernateFactory.getSessionFactory().openSession();
    Transaction tx1 = session.beginTransaction();
    Query query = session.createQuery("delete from Point where session_id = :session_id");
    query.setParameter("session_id", session_id);

    query.executeUpdate();
    tx1.commit();
    session.close();
  }

  public List<Point> findAll(String session_id) {
    Session session = HibernateFactory.getSessionFactory().openSession();

    Query query = session.createQuery("from Point where session_id = :session_id");
    query.setParameter("session_id", session_id);

    return (List<Point>) query.list();
  }

  public int getMaxId() {
    Session session = HibernateFactory.getSessionFactory().openSession();

    DetachedCriteria maxId = DetachedCriteria.forClass(Point.class)
        .setProjection(Projections.max("id"));

    Point maxPoint = (Point) session.createCriteria(Point.class)
        .add(Property.forName("id").eq(maxId))
        .uniqueResult();

    return maxPoint == null ? 0 : maxPoint.getId();
  }
}
