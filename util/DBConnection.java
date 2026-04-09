package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Rahul@1784";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getConfig("sms.db.url", "SMS_DB_URL", DEFAULT_URL);
        String user = getConfig("sms.db.user", "SMS_DB_USER", DEFAULT_USER);
        String password = getConfig("sms.db.password", "SMS_DB_PASSWORD", DEFAULT_PASSWORD);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found. Ensure mysql-connector-j is available in the classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("Unable to connect to MySQL using " + url + ". Configure credentials with SMS_DB_URL, SMS_DB_USER and SMS_DB_PASSWORD if needed. " + e.getMessage(), e);
        }
    }

    private static String getConfig(String propertyKey, String envKey, String fallback) {
        String propertyValue = System.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return fallback;
    }
}
