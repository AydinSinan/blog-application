import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class MySQLIntegrationTest {
    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.28")
            .withDatabaseName("taksdb")
            .withUsername("root")
            .withPassword("secret");

    @Test
    void testMySQLContainer() throws SQLException {
        assertTrue(mysqlContainer.isRunning());

        // MySQL bağlantısı oluştur
        String jdbcUrl = mysqlContainer.getJdbcUrl();
        String username = mysqlContainer.getUsername();
        String password = mysqlContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            // Test veritabanı işlemleri
            statement.executeUpdate("CREATE TABLE test_table (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255));");
            statement.executeUpdate("INSERT INTO test_table (name) VALUES ('Test Data');");

            // Veritabanı işlemlerini kontrol et
            var resultSet = statement.executeQuery("SELECT COUNT(*) FROM test_table;");
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }
}
