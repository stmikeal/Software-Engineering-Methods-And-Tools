package beans;

import static java.lang.System.exit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseHandler {

  static final String jdbcURL = "jdbc:oracle:thin:@localhost:1521:orbis";

  protected DatabaseHandler(){
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Oracle JDBC Driver is not found");
      e.printStackTrace();

    }

    Connection connection = null;
    try {
      connection = DriverManager.getConnection(jdbcURL, "s312515", "mej858");
    } catch (SQLException e) {
      System.out.println("Connection Failed : " + e.getMessage());
    }
    if (connection != null) {
      System.out.println("--- Connected to DB successfully ---");
    } else {
      System.out.println("Failed to make connection!");
    }
//    connection.close();
  }

}
