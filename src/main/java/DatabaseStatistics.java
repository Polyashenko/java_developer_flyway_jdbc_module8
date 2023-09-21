import java.io.PrintStream;
import java.sql.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DatabaseStatistics {
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

    private final String url;
    private final String username;
    private final String password;

    public DatabaseStatistics(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void printResidentsWithOwnershipAndAccessPermissions() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
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
