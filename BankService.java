import java.util.ArrayList;
import java.util.List;

public class BankService {
    private List<BankAccount> accounts = new ArrayList<>();

    private void saveToDatabase() {
        DatabaseManager.saveAccounts(accounts);
    }

    public BankService() {
        this.accounts = new ArrayList<>();
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(BankAccount acc) {
        accounts.add(acc);
        saveToDatabase();
    }

    public BankAccount verify(String accNumber, String pin) {
        for (BankAccount account : accounts) {
            if (account.getAccNumber().equals(accNumber) && account.getPin().equals(pin)) {
                return account;
            }
        }
        return null;
    }

    public boolean accExists(String accNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccNumber().equals(accNumber)) {
                return true;
            }
        }
        return false;
    }

    public boolean closeAcc(String accNumber) {
        boolean removed = accounts.removeIf(account -> account.getAccNumber().equals(accNumber));
        if (removed) saveToDatabase();
        return removed;
    }
}
