import org.flywaydb.core.Flyway;

public class DatabaseMigration {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseMigration(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void migrate() {
        Flyway.configure()
                .dataSource(url, username, password)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}
