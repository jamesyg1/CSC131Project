package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Test Connection Sucessful");

            // Simple table creation syntax
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INTEGER PRIMARY KEY, name TEXT)");

            // Insert a row syntax
            stmt.execute("INSERT INTO test_table (name) VALUES ('Hello SQLite Test Connection')");

            // ResultsSet meakes it read it back
            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
            while (rs.next()) {
                System.out.println("Row: " + rs.getInt("id") + " | " + rs.getString("name"));
            }
            // Error catch
        } catch (Exception e) {
            System.out.println("ERRORRRRRRR " + e.getMessage());
        }
    }
}