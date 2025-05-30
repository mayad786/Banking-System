import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL");
            return;
        }

        BankService bankService = new BankService();
        List<BankAccount> accounts = DatabaseManager.loadAccounts();
        accounts.forEach(bankService::addAccount);

        Menu menu = new Menu(bankService);
        List<BankAccount> loaded = DatabaseManager.loadAccounts();
        System.out.println("Found " + loaded.size() + " accounts in database");
        menu.showMainMenu();
    }
}
