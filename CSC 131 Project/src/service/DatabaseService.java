package service;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Item;
import model.Receipt;
import model.User;
import model.UserFactory;

/*
 * Tables created on first run:
 *   users   (id, name, email, password)
 *   receipts(id, user_id, name, date)
 *   items   (id, receipt_id, name, count, price)
 */
public class DatabaseService {

    private static DatabaseService instance;

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // Connection

    private static final String DB_URL = "jdbc:sqlite:app.db";
    private Connection conn;

    private DatabaseService() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection has been made: " + DB_URL);
            initTables();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database check error message: " + e.getMessage());
        }
    }

    private void initTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                name     TEXT    NOT NULL UNIQUE,
                email    TEXT    NOT NULL,
                password TEXT    NOT NULL
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS receipts (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                name    TEXT    NOT NULL,
                date    TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS items (
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                receipt_id INTEGER NOT NULL,
                name       TEXT    NOT NULL,
                count      INTEGER NOT NULL,
                price      REAL    NOT NULL,
                FOREIGN KEY (receipt_id) REFERENCES receipts(id)
            )
        """);

        stmt.close();
        System.out.println("Tables initialised.");
    }

    // New user method retun -1 if it fails
    public int insertUser(String name, String email, String password) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("insertUser failed: " + e.getMessage());
        }
        return -1;
    }

    //This loads the user into the hashmap adding onto our hashmap idea
    public HashMap<String, User> loadAllUsers() {
        HashMap<String, User> map = new HashMap<>();
        String sql = "SELECT id, name, email, password FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int    id       = rs.getInt("id");
                String name     = rs.getString("name");
                String email    = rs.getString("email");
                String password = rs.getString("password");
                map.put(name, UserFactory.createUser(id, email, password, name));
            }
        } catch (SQLException e) {
            System.err.println("loadAllUsers failed: " + e.getMessage());
        }
        return map;
    }

    //Duplicate user name checker
    public boolean usernameExists(String name) {
        String sql = "SELECT 1 FROM users WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("usernameExists failed: " + e.getMessage());
        }
        return false;
    }

  //Receipt operation

    public int insertReceipt(User owner, String name, String date, List<Item> items) {
        String sql = "INSERT INTO receipts (user_id, name, date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, owner.getUserID());
            ps.setString(2, name);
            ps.setString(3, date);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int receiptId = keys.getInt(1);
                for (Item item : items) {
                    insertItem(receiptId, item);
                }
                return receiptId;
            }
        } catch (SQLException e) {
            System.err.println("insertReceipt failed: " + e.getMessage());
        }
        return -1;
    }

      //Insert a single item into an existing receipt.
    public void insertItem(int receiptId, Item item) {
        String sql = "INSERT INTO items (receipt_id, name, count, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ps.setString(2, item.getName());
            ps.setInt(3, item.getCount());
            ps.setDouble(4, item.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("insertItem failed: " + e.getMessage());
        }
    }

     //Load all receipts that belong to the user inc their items
    public List<Receipt> loadReceiptsForUser(User user) {
        List<Receipt> list = new ArrayList<>();
        String sql = "SELECT id, name, date FROM receipts WHERE user_id = ? ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getUserID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int    dbId = rs.getInt("id");
                String name = rs.getString("name");
                String date = rs.getString("date");

                List<Item> items = loadItemsForReceipt(dbId);
                Receipt receipt = new Receipt(user, items, 0.0, name, date);
                // Override the random receiptID with the DB primary key so it is stable
                receipt.setReceiptID(dbId);
                list.add(receipt);
            }
        } catch (SQLException e) {
            System.err.println("loadReceiptsForUser failed: " + e.getMessage());
        }
        return list;
    }

    private List<Item> loadItemsForReceipt(int receiptId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT name, count, price FROM items WHERE receipt_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getString("name"), rs.getInt("count"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            System.err.println("loadItemsForReceipt failed: " + e.getMessage());
        }
        return items;
    }

    //Receipt deletion method
    public void deleteReceipt(int receiptId) {
        try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM items WHERE receipt_id = ?");
             PreparedStatement ps2 = conn.prepareStatement("DELETE FROM receipts WHERE id = ?")) {
            ps1.setInt(1, receiptId);
            ps1.executeUpdate();
            ps2.setInt(1, receiptId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteReceipt failed: " + e.getMessage());
        }
    }

        // Error checker
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("close failed: " + e.getMessage());
        }
    }
}
