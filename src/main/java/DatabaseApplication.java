public class DatabaseApplication {

    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/osbb";
    //The username for the database connection.
    public static final String USERNAME = "root";
    // The password for the database connection.
    public static final String PASSWORD = "rootroot";

    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        new DatabaseMigration(JDBC_URL, USERNAME,PASSWORD).migrate();
        new DatabaseStatistics(JDBC_URL, USERNAME,PASSWORD).printResidentsWithOwnershipAndAccessPermissions();
    }
}
