import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseMigration {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/osbb1";
    //The username for the database connection.
    public static final String USERNAME = "root";
    // The password for the database connection.
    public static final String PASSWORD = "root";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }

        Flyway.configure()
                .dataSource(JDBC_URL,USERNAME,PASSWORD)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}
