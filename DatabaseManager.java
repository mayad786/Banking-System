import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/banksystemschema";
    private static final String USER = "root";
    private static final String PASSWORD = "123530";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL driver not found!");
            e.printStackTrace();
        }
    }

    public static List<BankAccount> loadAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                accounts.add(new BankAccount(
                        rs.getString("accnumber"),
                        rs.getString("pin"),
                        rs.getString("accholdername"),
                        rs.getInt("balance"),
                        rs.getBoolean("isadmin")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database load error: " + e.getMessage());
        }
        return accounts;
    }

    public static void saveAccounts(List<BankAccount> accounts) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("TRUNCATE TABLE users");

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO users (accnumber, pin, accholdername, balance, isadmin) " + "VALUES (?, ?, ?, ?, ?)");

            for (BankAccount account : accounts) {
                pstmt.setString(1, account.getAccNumber());
                pstmt.setString(2, account.getPin());
                pstmt.setString(3, account.getAccHolderName());
                pstmt.setInt(4, account.getBalance());
                pstmt.setBoolean(5, account.isAdmin());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Database save error: " + e.getMessage());
        }
    }
}
