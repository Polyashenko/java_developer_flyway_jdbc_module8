import org.flywaydb.core.Flyway;

import java.io.PrintStream;
import java.sql.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DatabaseMigration {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/osbb1";
    //The username for the database connection.
    public static final String USERNAME = "root";
    // The password for the database connection.
    public static final String PASSWORD = "root";

    public static final String RESIDENTS_WITH_OWNERSHIP_AND_ACCESS_PERMISSIONS_QUERY = "SELECT" +
            " p.name AS name," +
            " p.email AS email," +
            " b.address AS address," +
            " a.number AS number," +
            " a.area AS area" +
            " FROM" +
            " person AS p" +
            " JOIN aparnment_ownership AS o ON p.id = o.person_id" +
            " JOIN apartment AS a ON o.apartment_id = a.id" +
            " JOIN building AS b ON a.building_id = b.id" +
            " JOIN resident AS r ON p.id = r.person_id" +
            " WHERE" +
            " r.entry_right = 1" +
            " GROUP BY" +
            " p.id, b.address, a.number, a.area" +
            " HAVING" +
            " COUNT(a.id) < 2";

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
        printResidentsWithOwnershipAndAccessPermissions();
    }

    public static void printResidentsWithOwnershipAndAccessPermissions(){
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(RESIDENTS_WITH_OWNERSHIP_AND_ACCESS_PERMISSIONS_QUERY);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PrintStream out = new PrintStream(System.out, true, UTF_8); // true = autoflush
                    out.print("Name: ");
                    out.print(resultSet.getString("name"));
                    out.print(", email: ");
                    out.print(resultSet.getString("email"));
                    out.print(", address: ");
                    out.print(resultSet.getString("address"));
                    out.print(", number: ");
                    out.print(resultSet.getString("number"));
                    out.print(", area: ");
                    out.print(resultSet.getString("area"));
                    out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
